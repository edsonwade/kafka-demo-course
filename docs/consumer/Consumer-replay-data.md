O "consumer replay" de dados no Kafka permite que você reprocessar mensagens a partir de um determinado ponto no log.
Isso pode ser útil para corrigir erros ou reprocessar dados com lógica atualizada.

Aqui está um exemplo básico de como configurar um consumidor Kafka para reprocessar dados a partir de um offset
específico:

1. **Configurar o consumidor para ler a partir do início do log**:
   ```java
   import org.apache.kafka.clients.consumer.ConsumerConfig;
   import org.apache.kafka.clients.consumer.ConsumerRecord;
   import org.apache.kafka.clients.consumer.ConsumerRecords;
   import org.apache.kafka.clients.consumer.KafkaConsumer;

   import java.time.Duration;
   import java.util.Collections;
   import java.util.Properties;
     @SuppressWarnings("all")
   public class KafkaConsumerReplay {

       public static void main(String[] args) {
           Properties props = new Properties();
           props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
           props.put(ConsumerConfig.GROUP_ID_CONFIG, "your-group-id");
           props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
           props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
           props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Ler do início do log

           KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
           consumer.subscribe(Collections.singletonList("your-topic"));

           while (true) {
               ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
               for (ConsumerRecord<String, String> record : records) {
                   System.out.printf("Offset: %d, Key: %s, Value: %s%n", record.offset(), record.key(), record.value());
                   // Processar a mensagem
               }
           }
       }
   }
   ```

2. **Configurar o consumidor para lançar uma exceção se não houver mensagens**:
   ```java

   import org.apache.kafka.clients.consumer.ConsumerConfig;
   import org.apache.kafka.clients.consumer.KafkaConsumer;

   import java.util.Properties;
   @SuppressWarnings("all")
   public class KafkaConsumerConfig {

       public static Properties getConsumerProps(String offsetReset) {
           Properties props = new Properties();
           props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
           props.put(ConsumerConfig.GROUP_ID_CONFIG, "your-group-id");
           props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
           props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
           props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetReset); // earliest, latest, ou none
           return props;
       }

       public static void main(String[] args) {
           Properties noneProps = getConsumerProps("none");

           try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(noneProps)) {
               // Use o consumidor conforme necessário
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
   ```

Neste exemplo, o consumidor é configurado para ler a partir do início do log (`earliest`) ou lançar uma exceção se não
houver mensagens (`none`). Isso permite reprocessar mensagens ou lidar com a ausência de mensagens conforme necessário.

## Configurando o Comportamento do Consumidor em Relação aos Offsets

Para configurar o comportamento do consumidor Kafka relativamente aos offsets, você pode usar a
propriedade `auto.offset.reset`. Aqui está como configurar cada um dos comportamentos mencionados:

1. **`earliest`**: O consumidor lê do início do log.
2. **`latest`**: O consumidor lê do fim do log.
3. **`none`**: Lança uma exceção se não houver mensagens.

Aqui está um exemplo de como configurar essas propriedades no consumidor Kafka:

```java
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

public class KafkaConsumerConfig {

    public static Properties getConsumerProps(String offsetReset) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "your-group-id");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetReset); // earliest, latest, or none
        return props;
    }

    public static void main(String[] args) {
        // Example usage
        Properties earliestProps = getConsumerProps("earliest");
        Properties latestProps = getConsumerProps("latest");
        Properties noneProps = getConsumerProps("none");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(earliestProps);
        // Use the consumer as needed
    }
}
```

Neste exemplo, a propriedade `auto.offset.reset` é configurada para `earliest`, `latest` ou `none`, dependendo do
comportamento desejado.