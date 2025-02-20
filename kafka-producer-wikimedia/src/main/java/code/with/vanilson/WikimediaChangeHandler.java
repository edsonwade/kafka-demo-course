package code.with.vanilson;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WikimediaChangeHandler
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-20
 */
public class WikimediaChangeHandler implements EventHandler {
    private static final Logger log = LoggerFactory.getLogger(WikimediaChangeHandler.class.getName());
    KafkaProducer<String, String> producer;
    String topic;

    public WikimediaChangeHandler(KafkaProducer<String, String> producer, String topic) {
        this.producer = producer;
        this.topic = topic;
    }

    @Override
    public void onOpen() {
        // nothinbg
    }

    @Override
    public void onClosed() {
        producer.close();
        log.info("Conexão fechada");
    }

    /**
     * Método onMessage que recebe um evento e uma mensagem de evento e envia a mensagem para o tópico do Kafka.
     *
     * @param event        o nome do evento recebido.
     * @param messageEvent o objeto MessageEvent que contém os dados da mensagem.
     */
    @Override
    public void onMessage(String event, MessageEvent messageEvent) {
        // Valida se o método onMessage envia dados para o tópico do Kafka
        log.info("Evento: {} ", event + " | Dados: " + messageEvent.getData());
        // Assinatura do método onMessage
        producer.send(new ProducerRecord<>(topic, messageEvent.getData()));
    }

    @Override
    public void onComment(String comment) {
        // nothinbg
    }

    @Override
    public void onError(Throwable t) {
        log.error("Erro on stream Reading : {} ", t.getMessage());
    }
}