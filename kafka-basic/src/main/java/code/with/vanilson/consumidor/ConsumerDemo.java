package code.with.vanilson.consumidor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * ConsumerDemo
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-19
 */
public class ConsumerDemo {
    private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class.getName());

    public static void main(String[] args) {
        Properties props = getProperties();
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            // Subscrever o consumidor Kafka ao tópico Kafka chamado "demo_java".
            consumer.subscribe(Collections.singletonList("demo_java"));
            // O consumidor Kafka lerá as mensagens do tópico Kafka chamado "demo_java" indefinidamente.
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    log.info("Polling for new messages");
                    // O consumidor Kafka lerá as mensagens do tópico Kafka chamado "demo_java" a cada 1000 milissegundos.
                    var records = consumer.poll(Duration.ofMillis(1000));
                    // O consumidor Kafka lerá as mensagens do tópico Kafka chamado "demo_java" e exibirá as mensagens no console.
                    for (ConsumerRecord<String, String> record : records) {
                        log.info("Key: {}, Value: {}", record.key(), record.value());
                        log.info("Partition: {}, Offset: {}", record.partition(), record.offset());
                    }
                } catch (Exception e) {
                    log.error("Error while consuming message from Kafka {}", e.getMessage());
                    Thread.currentThread().interrupt(); // Optionally handle the interruption
                }
            }

        }
    }

    private static Properties getProperties() {
        Properties props = new Properties();
        // Configuração do servidor Kafka (Broker) para o consumidor Kafka.
        props.setProperty("bootstrap.servers", "localhost:9092");
        // Configuração do grupo de consumidor Kafka. O grupo de consumidores é usado para identificar o consumidor
        // Kafka. O grupo de consumidores é usado para garantir que as mensagens sejam distribuídas entre os
        // consumidores Kafka de forma equilibrada.
        props.setProperty("group.id", "my-group");
        // Configuração do modo de recuperação de offset do consumidor Kafka. O modo de recuperação de offset é usado
        // para determinar de onde o consumidor Kafka deve começar a ler as mensagens.
        // O modo de recuperação de offset pode ser definido como "none", "earliest" ou "latest".
        // Se o modo de recuperação de offset for definido como "none", o consumidor Kafka não lerá as mensagens.
        // Se o modo de recuperação de offset for definido como "earliest", o consumidor Kafka lerá as mensagens desde o início.
        // Se o modo de recuperação de offset for definido como "latest", o consumidor Kafka lerá as mensagens desde o último offset.
        props.setProperty("auto.offset.reset", "earliest");
        // Configuração do serializador de chave do consumidor Kafka. O serializador de chave é usado para serializar a chave da mensagem.
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // Configuração do serializador de valor do consumidor Kafka. O serializador de valor é usado para serializar o valor da mensagem.
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }
}