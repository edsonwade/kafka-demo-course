package code.with.vanilson.webflux.rest;

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
@RequestMapping(path = "/send-message")
public class MessagerController {

    private final KafkaProducer kafkaProducer;

    public MessagerController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        kafkaProducer.sendMessage(message);
        return ResponseEntity.ok("Message Queued Successfully");

    }
}