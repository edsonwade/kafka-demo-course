package code.with.vanilson;

import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.opensearch.OpenSearchStatusException;
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
public class OpenSearchConsumer {
    private static final Logger log = LoggerFactory.getLogger(OpenSearchConsumer.class.getName());
    public static final String WIKIMEDIA = "wikimedia";

    public static void main(String[] args) {
        //first create an opensearch client
        try (RestHighLevelClient openSearchClient = OpenSearchClientConsumer.createOpenSearchClient();
             KafkaConsumer<String, String> consumer = KafkaConsumerClient.createKafkaConsumer()) {
            // check if the index exists already in opensearch cluster if not create it else log that it exists already.
            boolean indexExists =
                    openSearchClient.indices().exists(new GetIndexRequest(WIKIMEDIA), RequestOptions.DEFAULT);
            if (!indexExists) {
                CreateIndexRequest request = new CreateIndexRequest(WIKIMEDIA);
                openSearchClient.indices().create(request, RequestOptions.DEFAULT);
                log.info("The wikimedia index already exists");
            } else {
                log.error("The wikimedia index already exists");

            }
            // we need to subscribe to the topic we want to consume from in this case wikimedia-recentchange topic
            consumer.subscribe(Collections.singleton("wikimedia-recentchange"));
            // poll for new data
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
                int recordCount = records.count();
                log.info("Received {} {}", recordCount, " records");

                if (recordCount == 0) {
                    continue;
                }
                // loop through the records and send them to opensearch

                for (var record : records) {
                    // send the data to opensearch
                    indexRecord(record, openSearchClient);

                }

            }

        } catch (IOException e) {
            log.error("An error occurred:{} ", e.getMessage());
        }
    }

    /**
     * Method to send data to opensearch cluster using the rest high level client provided by the opensearch java client
     * library, and the data is sent as a JSON string to the opensearch cluster using the index method. The index method
     * is used to send data to the cluster. The index method takes an IndexRequest object as a parameter, and the index
     * request object takes the index name and the data to be sent to the cluster as a JSON string.
     * The index method returns an IndexResponse object which contains the id of the document that was sent to the cluster.
     * The id of the document is then logged to the console.
     * If an error occurs while sending the data to the cluster, an OpenSearchStatusException is thrown and the error message
     * is logged to the console.
     *
     * @param record           the record to be sent to the opensearch cluster
     * @param openSearchClient the opensearch client to be used to send the data to the cluster
     * @throws IOException if an error occurs while sending the data to the cluster
     */
    private static void indexRecord(ConsumerRecord<String, String> record,
                                    RestHighLevelClient openSearchClient)
            throws IOException {
        //Idepomtency check to ensure that the data is not sent to the cluster more than once.
        //define an id  usin kafka record coordinates to ensure that the data is sent only once
        // this is done by creating a unique id for each record using the kafka record coordinates
        // and then checking if the record has been sent to the cluster before
        // if it has been sent before then we skip it
        // if it has not been sent before then we send it to the cluster
        //Strategy 1
        //String id = record.topic() + "_" + record.partition() + "_" + record.offset();

        try {
            //Strategy 2 , where we extract the id from the json vallue
            String id = extractIdFromJsonValue(record.value());
            IndexRequest indexRequest = new IndexRequest(WIKIMEDIA)
                    .source(record.value(), XContentType.JSON)
                    .id(id);

            var indexResponse = openSearchClient.index(indexRequest, RequestOptions.DEFAULT);
            log.info("INFO opensearchConsumer -{} ", indexResponse.getId());
        } catch (OpenSearchStatusException e) {
            log.error("An error occurred while sending data to opensearch: {}", e.getMessage());
        }
    }

    /**
     * Method to extract the id from the json value of the record to be sent to the opensearch cluster.
     * The method uses the JsonParser class from the Gson library to parse the json value of the record
     * and extract the id from the json value.
     * The method returns the id of the record as a string.
     *
     * @param record the record to be sent to the opensearch cluster
     * @param json   the json value of the record to be sent to the opensearch cluster
     * @return the id of the record as a string
     */
    private static String extractIdFromJsonValue(String json) {
        //gson library
        return JsonParser.parseString(json)
                .getAsJsonObject()
                .get("meta")
                .getAsJsonObject()
                .get("id")
                .getAsString();

    }
}
