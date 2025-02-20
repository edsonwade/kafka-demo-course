package code.with.vanilson.produtor;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * ProducerDemoKeys
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-18
 */
public class ProducerDemoKeys {
    private static final Logger log = LoggerFactory.getLogger(ProducerDemoKeys.class.getName());

    public static void main(String[] args) {
        Properties properties = getProperties();
        // Criar uma mensagem para enviar para o tópico Kafka. A mensagem é composta por um valor e uma chave.
        // A chave é usada para garantir que a mensagem com a mesma chave seja enviada para a mesma partição.
        // Se a chave for nula, o Kafka escolherá aleatoriamente uma partição para enviar a mensagem.
        // Se a chave não for nula, o Kafka garantirá que todas as mensagens com a mesma chave sejam enviadas para a mesma partição.
        // Para melhorar o desempenho, o produtor usa o que é chamado de sticky partitioning, onde várias mensagens são enviadas para a mesma partição até que um certo limite seja atingido, antes de mudar para outra partição.
        // O particionador Round-Robin é usado para distribuir as mensagens de forma equilibrada entre todas as partições disponíveis.
        // O particionador Round-Robin é o particionador padrão do Kafka.
        // O particionador Round-Robin é usado quando a chave é nula.
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            // Enviar 10 mensagens para o tópico Kafka chamado "demo_java". Isto é que chamamos sticky partitioning,
            // onde a mesma chave é enviada para a mesma partição.
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i <= 10; i++) {
                    String topic = "demo_java";
                    String key = "truck_id " + i;
                    String value = "Hello world " + i;
                    ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
                    producer.send(record, (recordMetadata, e) -> {
                        if (e == null) {
                            log.info("key :{}", key + "| Partition:{} " + recordMetadata.partition());
                        } else {
                            log.error("Error while sending message to Kafka {} ", e.getMessage());
                        }
                    });
                }
                Thread.sleep(500);
                producer.flush();
            }
        } catch (InterruptedException e) {
            log.error("Thread exception error ", e);
            Thread.currentThread().interrupt();
        }
    }

    public static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        // Configurar o número de tentativas de reenvio
        properties.setProperty("retries", "3");
        // Configurar o tempo de espera entre tentativas
        properties.setProperty("retry.backoff.ms", "100");
        // Habilitar idempotência para evitar mensagens duplicadas
        properties.setProperty("enable.idempotence", String.valueOf(Boolean.TRUE));
        // Configurar o tempo limite de entrega
        properties.setProperty("delivery.timeout.ms", "120000"); // 2 minutos
        return properties;
    }
}