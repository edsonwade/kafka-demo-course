package code.with.vanilson.consumidor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

/**
 * ConsumerThread
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-19
 */
@SuppressWarnings("all")
public class ConsumerThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ConsumerThread.class.getName());
    private final KafkaConsumer<String, String> consumer;
    private final List<String> topics;

    public ConsumerThread(Properties props, List<String> topics) {
        this.consumer = new KafkaConsumer<>(props);
        this.topics = topics;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(topics);
            while (true) {
                var records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Key: %s, Value: %s%n", record.key(), record.value());
                    System.out.printf("Partition: %d, Offset: %d%n", record.partition(), record.offset());
                }
            }
        } catch (WakeupException e) {
            // Ignorar exceção se estiver fechando
        } finally {
            consumer.close();
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }
}