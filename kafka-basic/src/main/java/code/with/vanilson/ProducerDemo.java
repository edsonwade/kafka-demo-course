package code.with.vanilson;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Producer Demo!
 * Criar um produtor Kafka (Producer) e enviar uma mensagem para um tópico Kafka.
 * Criar o topic em um terminal com o comando: kafka-topics --create --topic demo_java --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
 * <p>
 * Para verficar se a mensagem foi enviada com sucesso, execute o comando abaixo no terminal:
 * kafka-console-consumer --bootstrap-server localhost:9092 --topic demo_java --from-beginning
 */
public class ProducerDemo {
    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getName());

    public static void main(String[] args) {
        // Criar propriedades do produtor Kafka (Producer)
        Properties properties = getProperties();
        // Criar um produtor Kafka (Producer) com as propriedades configuradas acima
        // (properties) e a chave e valor do tipo ‘String’.
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            // Enviar uma mensagem para o tópico Kafka chamado "demo_java".
            // Mas tem que ser criado antes no terminal o topic com o comando: kafka-topics --create --topic
            // demo_java --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

            // Criar um objeto ProducerRecord com o tópico Kafka, chave e valor.
            //Quando usamos a chave, o Kafka garante que a mensagem com a mesma chave será enviada para a mesma
            // partição.
            // Se a chave for nula, o Kafka escolherá aleatoriamente uma partição para enviar a mensagem.
            ProducerRecord<String, String> record = new ProducerRecord<>("demo_java", "key1", "Message 1 for key1");
            ProducerRecord<String, String> record2 = new ProducerRecord<>("demo_java", "key1", "Message 2 for key1");

            // Enviar a mensagem para o tópico Kafka.
            producer.send(record);
            producer.send(record2);
            log.info("Message sent successfully!");
            // Diz ao produtor para enviar todas as mensagens pendentes para o tópico Kafka até o momento -
            // sincrónico (bloqueante).
            producer.flush();

        } catch (Exception e) {
            log.error("Error while sending message to Kafka", e);
        }

    }

    /**
     * Método para configurar as propriedades do produtor Kafka (Producer).
     *
     * @return Properties - propriedades do produtor Kafka (Producer).
     */
    private static Properties getProperties() {
        Properties properties = new Properties();
        // Configurar propriedades do produtor Kafka (Producer) - bootstrap.servers é o endereço do servidor Kafka
        // (Broker) que o produtor deve se conectar para enviar mensagens.
        // Neste caso, o servidor Kafka está sendo executado localmente na porta 9092.
        properties.setProperty("bootstrap.servers", "localhost:9092");
        // Configurar propriedades do produtor Kafka (Producer) - key.serializer é a classe que serializa a chave
        // que será enviada para o tópico Kafka.
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // Configurar propriedades do produtor Kafka (Producer) - value.serializer é a classe que serializa o valor
        // que será enviado para o tópico Kafka.
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

}
