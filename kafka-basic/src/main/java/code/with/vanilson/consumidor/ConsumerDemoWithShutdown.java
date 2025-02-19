package code.with.vanilson.consumidor;

import code.with.vanilson.utils.PropertiesUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

/**
 * ConsumerDemoWithShutdown
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-19
 */

@SuppressWarnings("all")
public class ConsumerDemoWithShutdown {
    private static final Logger log = LoggerFactory.getLogger(ConsumerDemoWithShutdown.class.getName());
    private static final Properties props = PropertiesUtils.getProperties();

    public static void main(String[] args) {

        try (KafkaConsumer<String, String> consumers = new KafkaConsumer<>(props)) {
            //Cria um thread principal para o consumidor Kafka que é o thread main. O thread main é o thread que executa o método main.
            final Thread mainThread = Thread.currentThread();
            // Adiciona um hook de shutdown para o consumidor Kafka.
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Detected a shutdown consumer, let's exit by calling consumer.wakeup()");
                consumers.wakeup();
                // Espera o thread principal terminar.
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    log.error("Error while shutdown consumer", e);
                    Thread.currentThread().interrupt();
                }
            }));

            pollMessages(consumers);
        }
    }

    /**
     * Método para ler as mensagens do tópico Kafka chamado "demo_java".
     * O consumidor Kafka lerá as mensagens do tópico Kafka chamado "demo_java" indefinidamente.
     * O consumidor Kafka lerá as mensagens do tópico Kafka chamado "demo_java" a cada 1000 milissegundos.
     * O consumidor Kafka lerá as mensagens do tópico Kafka chamado "demo_java" e exibirá as mensagens no console.
     *
     * @param consumers - consumidor Kafka.
     */
    static void pollMessages(KafkaConsumer<String, String> consumers) {
        try {
            // Subscrever o consumidor Kafka ao tópico Kafka chamado "demo_java".
            consumers.subscribe(List.of("demo_java"));
            // O consumidor Kafka lerá as mensagens do tópico Kafka chamado "demo_java" indefinidamente.
            pollIndefinitely(consumers);
        } catch (WakeupException e) {
            log.info("Consumer is starting to shutdown");
            // Ignore exception if closing
        } catch (Exception e) {
            log.error("Unexpected exception to the consumer", e);
        }
        log.info("Consumer has been closed");
    }

    /**
     * Método para ler as mensagens do tópico Kafka chamado "demo_java" indefinidamente.
     *
     * @param consumers - consumidor Kafka.
     */

    public static void pollIndefinitely(KafkaConsumer<String, String> consumers) {
        while (true) {
            log.info("Polling for new messages");
            var records = consumers.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                log.info("Key: {}, Value: {}", record.key(), record.value());
                log.info("Partition: {}, Offset: {}", record.partition(), record.offset());
            }
        }
    }
}
