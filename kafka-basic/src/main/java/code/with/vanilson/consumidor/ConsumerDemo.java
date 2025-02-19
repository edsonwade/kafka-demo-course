package code.with.vanilson.consumidor;

import code.with.vanilson.PropertiesUtils;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

import static code.with.vanilson.consumidor.ConsumerDemoWithShutdown.pollIndefinitely;

/**
 * ConsumerDemo
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-19
 */
public class ConsumerDemo {

    public static void main(String[] args) {
        Properties props = PropertiesUtils.getProperties();

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            // Subscrever o consumidor Kafka ao tópico Kafka chamado "demo_java".
            consumer.subscribe(Collections.singletonList("demo_java"));
            // O consumidor Kafka lerá as mensagens do tópico Kafka chamado "demo_java" indefinidamente.

            pollIndefinitely(consumer);

        }
    }

}