package code.with.vanilson.webflux.rest;

import code.with.vanilson.webflux.payload.Student;
import code.with.vanilson.webflux.producer.KafkaJsonProducer;
import code.with.vanilson.webflux.producer.KafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MessagerController
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-24
 */
@RestController
@RequestMapping(path = "/message")
public class MessagerController {

    private final KafkaProducer kafkaProducer;
    private final KafkaJsonProducer producer;

    public MessagerController(KafkaProducer kafkaProducer, KafkaJsonProducer producer) {
        this.kafkaProducer = kafkaProducer;
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        kafkaProducer.sendMessage(message);
        return ResponseEntity.ok("Message Queued Successfully");

    }

    @PostMapping(value = "/student")
    public ResponseEntity<String> sendJsonMessage(@RequestBody Student student) {
        producer.sendJsonMessage(student);
        return ResponseEntity.ok("Message sent to kafka topic");

    }
}