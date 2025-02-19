package code.with.vanilson.consumidor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * ConsumerGroup
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-19
 */
public class ConsumerGroup {
    private static final Logger log = LoggerFactory.getLogger(ConsumerGroup.class.getName());

    private final List<ConsumerThread> consumers;
    private final List<Thread> threads;

    public ConsumerGroup(int numConsumers, String groupId, List<String> topics, Properties props) {
        consumers = new ArrayList<>(numConsumers);
        threads = new ArrayList<>(numConsumers);
        props.setProperty("group.id", groupId);

        for (int i = 0; i < numConsumers; i++) {
            ConsumerThread consumerThread = new ConsumerThread(props, topics);
            consumers.add(consumerThread);
            threads.add(new Thread(consumerThread));
        }
    }

    public void start() {
        for (Thread thread : threads) {
            log.info("Starting consumer thread");
            thread.start();
        }
    }

    public void shutdown() {
        for (ConsumerThread consumer : consumers) {
            log.info("Shutting down consumer thread");
            consumer.shutdown();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        List<String> topics = Collections.singletonList("demo_java");
        ConsumerGroup consumerGroup = new ConsumerGroup(3, "my-group", topics, props);

        Runtime.getRuntime().addShutdownHook(new Thread(consumerGroup::shutdown));
        log.info("Starting consumer group");

        consumerGroup.start();
    }

}