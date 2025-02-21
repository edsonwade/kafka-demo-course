package code.with.vanilson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.opensearch.OpenSearchStatusException;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.bulk.BulkResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;

@SuppressWarnings("all")
/**
 * This class is responsible for consuming messages from a Kafka topic and indexing them into OpenSearch.
 */
public class OpenSearchConsumer {
    private static final Logger log = LoggerFactory.getLogger(OpenSearchConsumer.class.getName());
    private static final String WIKIMEDIA_INDEX = "wikimedia";
    private static final String TOPIC = "wikimedia-recentchange";

    /**
     * The main method to start the consumer.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try (RestHighLevelClient openSearchClient = OpenSearchClientConsumer.createOpenSearchClient();
             KafkaConsumer<String, String> consumer = KafkaConsumerClient.createKafkaConsumer()) {

            final Thread mainThread = Thread.currentThread();

            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Detected a shutdown consumer, let's exit gracefully.");
                consumer.wakeup();
                try {
                    mainThread.join(); // Wait until the main consumer loop completes
                } catch (InterruptedException e) {
                    log.error("Error while shutdown consumer", e);
                    Thread.currentThread().interrupt();
                }
            }));

            // Check and create OpenSearch index if necessary
            if (!openSearchClient.indices().exists(new GetIndexRequest(WIKIMEDIA_INDEX), RequestOptions.DEFAULT)) {
                CreateIndexRequest createIndexRequest = new CreateIndexRequest(WIKIMEDIA_INDEX);
                openSearchClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                log.info("Created index: {}", WIKIMEDIA_INDEX);
            } else {
                log.info("Index already exists: {}", WIKIMEDIA_INDEX);
            }

            // Subscribe to Kafka topic
            consumer.subscribe(Collections.singleton(TOPIC));

            // Poll loop
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
                int recordCount = records.count();
                log.info("Received {} records", recordCount);

                if (recordCount > 0) {
                    BulkRequest bulkRequest = new BulkRequest();
                    for (ConsumerRecord<String, String> record : records) {
                        addRecordToBulkRequest(record, bulkRequest);
                    }

                    if (bulkRequest.numberOfActions() > 0) {
                        BulkResponse bulkResponse = openSearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                        if (!bulkResponse.hasFailures()) {
                            log.info("Inserted: {} records", bulkResponse.getItems().length);
                        } else {
                            log.error("Bulk request failed: {}", bulkResponse.buildFailureMessage());
                        }
                    }

                    // Commit offsets asynchronously
                    consumer.commitAsync((offsets, exception) -> {
                        if (exception == null) {
                            log.info("Offsets committed to Kafka");
                        } else {
                            log.error("Failed to commit offsets: {}", exception.getMessage());
                        }
                    });
                }

                try {
                    Thread.sleep(1000); // Control thread sleep interval
                } catch (InterruptedException e) {
                    log.error("Thread sleep interrupted: {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        } catch (IOException e) {
            log.error("An error occurred: {}", e.getMessage());
        } catch (WakeupException e) {
            log.info("Consumer is starting to shutdown");
        } catch (Exception e) {
            log.error("Unexpected exception to the consumer", e);
        }
        log.info("Consumer has been closed");
    }

    /**
     * Adds a record to the bulk request after normalizing the log_params field.
     *
     * @param record the consumer record containing the original JSON string
     * @param bulkRequest the bulk request to which the index request will be added
     */
    private static void addRecordToBulkRequest(ConsumerRecord<String, String> record, BulkRequest bulkRequest) {
        try {
            String originalString = record.value();
            String id = extractIdFromJsonValue(originalString);
            String normalizedString = serializeLogParams(originalString);

            IndexRequest indexRequest = new IndexRequest("wikimedia")
                    .source(normalizedString, XContentType.JSON)
                    .id(id);
            bulkRequest.add(indexRequest);
        } catch (OpenSearchStatusException e) {
            log.error("An error occurred while adding record to bulk request: {}", e.getMessage());
        }
    }

    /**
     * Serializes the log_params field in the original JSON string.
     *
     * @param originalString the original JSON string
     * @return the modified JSON string with the log_params field serialized
     */
    private static String serializeLogParams(String originalString) {
        JsonObject jsonObject = JsonParser.parseString(originalString).getAsJsonObject();

        if (jsonObject.has("log_params")) {
            // Get the log_params as an object (no serialization into a string)
            JsonElement logParams = jsonObject.get("log_params");

            // Add log_params back as an object
            jsonObject.add("log_params", logParams);
        }

        return jsonObject.toString();  // Return the modified JSON string
    }

    /**
     * Extracts the ID from the JSON value.
     *
     * @param json the JSON string
     * @return the extracted ID
     */

    private static String extractIdFromJsonValue(String json) {
        return JsonParser.parseString(json)
                .getAsJsonObject()
                .get("meta")
                .getAsJsonObject()
                .get("id")
                .getAsString();
    }
}
