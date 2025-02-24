package code.with.vanilson.webflux.producer;

import code.with.vanilson.webflux.payload.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
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
public class KafkaJsonProducer {

    @Value("${application.config.topics}")
    private String topic;
    private final KafkaTemplate<String, Student> kafkaTemplate;

    public KafkaJsonProducer(KafkaTemplate<String, Student> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendJsonMessage(Student student) {
        try {

            Message<Student> studentMessage = MessageBuilder
                    .withPayload(student)
                    .setHeader(KafkaHeaders.TOPIC, "json-topic")
                    .build();
            log.info("Sending message to gym topic : {}", studentMessage);
            kafkaTemplate.send(studentMessage);
        } catch (ClassCastException e) {
            log.error("Error while sending message to gym topic", e);
        }
    }
}