package code.with.vanilson.utils;

import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;

import java.util.Properties;

/**
 * PropertiesUtils
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-19
 */
public class PropertiesUtils {
    public static final String BOOTSTRAP_SERVERS = "localhost:9092";
    public static final String GROUP_ID = "my-group";
    public static final String OFFSET_RESET = "earliest";
    public static final String KEY_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    public static final String VALUE_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";

    private PropertiesUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Properties getProperties() {
        Properties props = new Properties();
        // Configuração do servidor Kafka (Broker) para o consumidor Kafka.
        props.setProperty("bootstrap.servers", BOOTSTRAP_SERVERS);
        // Configuração do grupo de consumidor Kafka. O grupo de consumidores é usado para identificar o consumidor
        // Kafka. O grupo de consumidores é usado para garantir que as mensagens sejam distribuídas entre os
        // consumidores Kafka de forma equilibrada.
        props.setProperty("group.id", GROUP_ID);
        // Configuração do modo de recuperação de offset do consumidor Kafka. O modo de recuperação de offset é usado
        // para determinar de onde o consumidor Kafka deve começar a ler as mensagens.
        // O modo de recuperação de offset pode ser definido como "none", "earliest" ou "latest".
        // Se o modo de recuperação de offset for definido como "none", o consumidor Kafka não lerá as mensagens.
        // Se o modo de recuperação de offset for definido como "earliest", o consumidor Kafka lerá as mensagens desde o início.
        // Se o modo de recuperação de offset for definido como "latest", o consumidor Kafka lerá as mensagens desde o último offset.
        props.setProperty("auto.offset.reset", OFFSET_RESET);
        // Configura o desserializador de chave do consumidor Kafka. O desserializador de chave é usado para converter a chave da mensagem de bytes para String.
        props.setProperty("key.deserializer", KEY_DESERIALIZER);
        // Podes
        // Substituir por props.setProperty("key.deserializer", StringDeserializer.class.getName()); é a mesma coisa.
        // Configura o desserializador de valor do consumidor Kafka. O desserializador de valor é usado para converter o valor da mensagem de bytes para String.
        props.setProperty("value.deserializer", VALUE_DESERIALIZER);

        // Configuração do particionador de atribuição do consumidor Kafka. O particionador de atribuição é usado para determinar como as partições são atribuídas aos consumidores Kafka.
        // O particionador de atribuição pode ser definido como "org.apache.kafka.clients.consumer.RangeAssignor" ou "org.apache.kafka.clients.consumer.RoundRobinAssignor".
        // Se o particionador de atribuição for definido como "org.apache.kafka.clients.consumer.RangeAssignor", as partições serão atribuídas aos consumidor Kafka com base num intervalo de partições.
        // Se o particionador de atribuição for definido como "org.apache.kafka.clients.consumer.RoundRobinAssignor", as partições serão atribuídas aos consumidores Kafka de forma circular.
        props.setProperty("partition.assignment.strategy", CooperativeStickyAssignor.class.getName()); // Podes
        // substituir por props.setProperty("partition.assignment.strategy", "org.apache.kafka.clients.consumer.CooperativeStickyAssignor"); é a mesma coisa.

        return props;
    }
}