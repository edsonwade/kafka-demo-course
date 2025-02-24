package code.with.vanilson.webflux.consumer;

import code.with.vanilson.webflux.payload.Student;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * KafkaConsumer
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-24
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class KafkaConsumer {

    //@KafkaListener(topics = "twitter-topic", groupId = "MyGroup")
    public void consumeMessage(String message) {
        log.info("Consuming message:{}  from topic: {}", message, "twitter-topic");
    }

    @KafkaListener(topics = "json-topic", groupId = "MyGroup")
    public void consumeJsonMessage(Student message) {
        log.info("Consuming json message :{}  from topic: {}", message.toString(), "json-topic");
    }

}