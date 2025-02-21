package code.with.vanilson;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

/**
 * KafkaConsumerClient
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-20
 */
public class KafkaConsumerClient {
    private KafkaConsumerClient() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Cria um consumidor Kafka com as propriedades padrão definidas no método getProperties ().
     *
     * @return KafkaConsumer<String, String> - consumidor Kafka.
     * @see #getProperties()
     */

    public static KafkaConsumer<String, String> createKafkaConsumer() {
        return new KafkaConsumer<>(getProperties());
    }

    /**
     * Retorna as propriedades padrão para o consumidor Kafka que são:
     * - bootstrap.servers: localhost:9092
     * - group.id: consumer-opensearch-demo
     * - auto.offset.reset: latest
     * - key.deserializer: org.apache.kafka.common.serialization.StringDeserializer
     * - value.deserializer: org.apache.kafka.common.serialization.StringDeserializer
     *
     * @return Properties - propriedades para o consumidor Kafka.
     * @see org.apache.kafka.clients.consumer.KafkaConsumer
     * @see org.apache.kafka.common.serialization.StringDeserializer
     * @see java.util.Properties
     * @see #createKafkaConsumer()
     * @see #getProperties()
     * @see #createKafkaConsumer()
     */

    public static Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "consumer-opensearch-demo");
        props.setProperty("auto.offset.reset", "latest");
        props.setProperty("enable.auto.commit", "false");
        props.setProperty("key.deserializer", StringDeserializer.class.getName());
        props.setProperty("value.deserializer", StringDeserializer.class.getName());

        return props;
    }
}