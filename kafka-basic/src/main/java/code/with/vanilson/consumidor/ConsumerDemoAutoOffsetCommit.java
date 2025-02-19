package code.with.vanilson.consumidor;

import code.with.vanilson.utils.PropertiesUtils;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static code.with.vanilson.consumidor.ConsumerDemoWithShutdown.pollMessages;

/**
 * ConsumerDemoGroupCooperative
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-19
 */
@SuppressWarnings("all")
public class ConsumerDemoAutoOffsetCommit {
    private static final Logger log = LoggerFactory.getLogger(ConsumerDemoAutoOffsetCommit.class.getName());
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
}