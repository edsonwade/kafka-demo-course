package code.with.vanilson;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * ProductorDemoAcks
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-18
 */
public class ProductorDemoAcks {
    private static final Logger log = LoggerFactory.getLogger(ProductorDemoAcks.class.getName());

    public static void main(String[] args) {
        // Exemplo com acks=0
        Properties propertiesAcks0 = getProperties("0");
        sendMessages(propertiesAcks0);

        // Exemplo com acks=1
        Properties propertiesAcks1 = getProperties("1");
        sendMessages(propertiesAcks1);

        // Exemplo com acks=all
        Properties propertiesAcksAll = getProperties("all");
        sendMessages(propertiesAcksAll);
    }

    private static Properties getProperties(String acks) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        properties.setProperty("acks", acks);
        return properties;
    }

    private static void sendMessages(Properties properties) {
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            for (int i = 0; i < 10; i++) {
                String topic = "acks_topic";
                String key = "key_" + i;
                String value = "Hello world!! knowlegde is power " + i;
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
                producer.send(record, (metadata, exception) -> {
                    if (exception == null) {
                        log.info("Sent message with key: {} to partition: {}, offset: {}", key, metadata.partition(),
                                metadata.offset());
                    } else {
                        log.error("Error while sending message: {}", exception.getMessage());
                    }
                });
            }
            producer.flush();
        }
    }
}