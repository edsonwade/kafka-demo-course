package code.with.vanilson;

import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
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

            // Check if the index exists, create it if it doesn't
            if (!openSearchClient.indices().exists(new GetIndexRequest(WIKIMEDIA_INDEX), RequestOptions.DEFAULT)) {
                CreateIndexRequest createIndexRequest = new CreateIndexRequest(WIKIMEDIA_INDEX);
                openSearchClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                log.info("Created index: {}", WIKIMEDIA_INDEX);
            } else {
                log.info("Index already exists: {}", WIKIMEDIA_INDEX);
            }

            // Subscribe to the topic
            consumer.subscribe(Collections.singleton(TOPIC));

            // Poll for new data
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

                // Sleep for 1000 milliseconds
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("Thread sleep interrupted: {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        } catch (IOException e) {
            log.error("An error occurred: {}", e.getMessage());
        }
    }

    /**
     * Adds a record to the bulk request.
     *
     * @param record      the consumer record
     * @param bulkRequest the bulk request
     */
    private static void addRecordToBulkRequest(ConsumerRecord<String, String> record, BulkRequest bulkRequest) {
        try {
            String id = extractIdFromJsonValue(record.value());
            IndexRequest indexRequest = new IndexRequest(WIKIMEDIA_INDEX)
                    .source(record.value(), XContentType.JSON)
                    .id(id);
            bulkRequest.add(indexRequest);
        } catch (OpenSearchStatusException e) {
            log.error("An error occurred while adding record to bulk request: {}", e.getMessage());
        }
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
