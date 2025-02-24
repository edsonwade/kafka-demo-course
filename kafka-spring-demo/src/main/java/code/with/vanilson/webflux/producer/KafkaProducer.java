package code.with.vanilson.webflux.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * KafkaProducer
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-24
 */

@Service
@Slf4j
public class KafkaProducer {

    @Value("${application.config.topics}")
    private String topic;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        log.info("Sending message to gym topic : {}", message);
        kafkaTemplate.send(topic, message);
    }
}