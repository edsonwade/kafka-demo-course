package code.with.vanilson.produtor;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * ProducerDemoWithCallback
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-18
 */
public class ProducerDemoWithCallback {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoWithCallback.class.getName());

    public static void main(String[] args) {

        // Criar propriedades do produtor Kafka (Producer)
        Properties properties = getProperty();

        // Enviar 10 mensagens para o tópico Kafka chamado "prod_topic" e "prod_topic_1". Isto é que chamamos
        // sticky pArtitioning, onde a mesma chave é enviada para a mesma partição.
        for (int i = 0; i < 10; i++) {

            // Criar um produtor Kafka (Producer) com as propriedades configuradas acima (properties) e a chave e valor do tipo ‘String’.
            try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
                // Enviar uma mensagem para o tópico Kafka chamado "demo_java". Mas tem que ser criado antes no terminal o topic com o comando: kafka-topics --create --topic demo_java --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
                // Criar um objeto ProducerRecord com o tópico Kafka, chave e valor. Quando usamos a chave, o Kafka garante que a mensagem com a mesma chave será enviada para a mesma partição. Se a chave for nula, o Kafka escolherá aleatoriamente uma partição para enviar a mensagem.
                ProducerRecord<String, String> record =
                        new ProducerRecord<>("twitter", "Hello twitter !! " + i);
                ProducerRecord<String, String> record2 =
                        new ProducerRecord<>("udemy", "Hello Udemy !!" + i);
                // Enviar a mensagem para o tópico Kafka.
                sendMessageToKafkaTopic(producer, record);
                sendMessageToKafkaTopic(producer, record2);
                // Diz ao produtor para enviar todas as mensagens pendentes para o tópico Kafka até o momento - sincrónico (bloqueante).
                producer.flush();
            } catch (ClassCastException e) {
                log.error("Error while sending message to Kafka {}", e.getMessage());
            }
        }
    }
    // Mensagens produzidas pelo produtor podem ter um valor nulo, mas não podem ter uma chave nula.
// Se a chave for nula, o Kafka escolherá aleatoriamente uma partição para enviar a mensagem usando o particionador Round-Robin.
// Ou seja, a mensagem será distribuída de forma equilibrada entre todas as partições disponíveis.
// Se a chave não for nula, o Kafka garantirá que todas as mensagens com a mesma chave serão enviadas para a mesma partição.
// Para melhorar o desempenho, o produtor usa o que é chamado de sticky partitioning, onde várias mensagens são enviadas para a mesma partição até que um certo limite seja atingido, antes de mudar para outra partição.

    /**
     * Método para enviar a mensagem para o tópico Kafka.
     * Se a exceção for nula, a mensagem foi enviada com sucesso.
     * Se a mensagem foi enviada com sucesso, o Kafka retornará os metadados da mensagem.
     * Se a exceção não for nula, um erro ocorreu ao enviar a mensagem para o tópico Kafka.
     *
     * @param producer - produtor Kafka (Producer).
     * @param record   - objeto ProducerRecord com o tópico Kafka, chave e valor.
     */
    private static void sendMessageToKafkaTopic(KafkaProducer<String, String> producer,
                                                ProducerRecord<String, String> record) {
        producer.send(record, (metadata, exception) -> {
            // Se a exceção for nula, a mensagem foi enviada com sucesso.
            if (exception == null) {
                // Se a mensagem foi enviada com sucesso, o Kafka retornará os metadados da mensagem.
                log.info("Recebendo novos metadados: \nTopic: {}\nPartition: {}\nOffset: {}\nTimestamp: {}",
                        metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp());
            } else {
                log.error("Error while producing message to Kafka: {}", exception.getMessage());
            }
        });
    }

    /**
     * Método para configurar as propriedades do produtor Kafka (Producer) - bootstrap.servers é o endereço do servidor Kafka
     * (Broker) que o produtor deve se conectar para enviar mensagens.
     * key.serializer é a classe de serialização da chave que será enviada para o tópico Kafka.
     * value.serializer é a classe de serialização do valor que será enviado para o tópico Kafka.
     *
     * @return Properties - propriedades do produtor Kafka (Producer).
     * @see Properties - propriedades do produtor Kafka (Producer).
     * @since 2025-02-18
     */

    private static Properties getProperty() {
        Properties properties = new Properties();
        // Configurar propriedades do produtor Kafka (Producer) - bootstrap.servers é o endereço do servidor Kafka
        properties.setProperty("bootstrap.servers", "localhost:9092");
        // Configurar propriedades do produtor Kafka (Producer) - acks é a confirmação que o líder da partição do
        // tópico Kafka enviou a mensagem com sucesso. O valor "all" é a configuração mais segura, pois garante que
        // a mensagem foi recebida com sucesso por todos os seguidores e líder da partição.
        // acks=0 - não há confirmação, a mensagem é enviada e esquecida.
        // acks=1 - confirmação do líder da partição.
        // acks=all - confirmação do líder da partição e todos os seguidores.
        // Mesmo que acks -1, o líder da partição envia uma confirmação de que a mensagem foi recebida com sucesso.
        properties.setProperty("acks", "all");

       // Configurar propriedades do produtor Kafka (Producer) - partitioner.class é a classe de particionador que
        // será usada para distribuir as mensagens entre as partições do tópico Kafka.
       properties.setProperty("partitioner.class", RoundRobinPartitioner.class.getName());

        // Configurar propriedades do produtor Kafka (Producer) - retries é o número de tentativas que o produtor
        // Kafka fará ao enviar mensagens para o tópico Kafka.
        properties.setProperty("retries", "3");
        // Configurar propriedades do produtor Kafka (Producer) - linger.ms é o tempo em milissegundos que o produtor
        // Kafka aguardará antes de enviar mensagens para o tópico Kafka.
        properties.setProperty("linger.ms", "1");
        // Configurar propriedades do produtor Kafka (Producer) - buffer.memory é o tamanho do buffer de memória que o
        // produtor Kafka usará para armazenar mensagens antes de enviá-las para o tópico Kafka.
        properties.setProperty("buffer.memory", "33554432");
        // Configurar propriedades do produtor Kafka (Producer) - batch.size é o tamanho do lote que o produtor Kafka
        // usará para enviar mensagens para o tópico Kafka.
        properties.setProperty("batch.size", "400");

        // Configurar propriedades do produtor Kafka (Producer) - key.serializer é a classe de serialização da chave que será enviada para o tópico Kafka.
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        // Configurar propriedades do produtor Kafka (Producer) - value.serializer é a classe de serialização do valor que será enviado para o tópico Kafka.
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        return properties;
    }
}