In Kafka, the `retries` configuration parameter is used to specify the number of retry attempts the producer will make in case of transient errors before giving up and failing to send the message. This helps in handling temporary issues like network glitches or broker unavailability.

Here is how developers typically handle retries in Kafka producers:

1. **Set the `retries` parameter**: Configure the number of retries in the producer properties.
2. **Configure `retry.backoff.ms`**: Set the backoff time between retry attempts.
3. **Idempotence**: Enable idempotence to ensure that retries do not result in duplicate messages.

Here is an example of how to configure these properties in a Kafka producer:

```java

public static void main(String[] args) {

    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9092");
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

// Set the number of retries
    properties.put("retries", 3);

// Set the backoff time between retries
    properties.put("retry.backoff.ms", 100);

// Enable idempotence to avoid duplicate messages
    properties.put("enable.idempotence", true);

    KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
}
```

In this example:
- `retries=3` specifies that the producer will retry up to 3 times in case of errors.
- `retry.backoff.ms=100` sets a 100 ms delay between retry attempts.
- `enable.idempotence=true` ensures that retries do not result in duplicate messages.

## PORTUGUESE
Para lidar com erros no produtor Kafka, os desenvolvedores geralmente configuram o parâmetro `retries` para especificar o número de tentativas de reenvio em caso de erros transitórios.
Aqui está um exemplo de como configurar essas propriedades num produtor Kafka:

```java
import org.apache.kafka.clients.producer.KafkaProducer;
import java.util.Properties;

public class KafkaProducerConfig {

    public static KafkaProducer<String, String> createProducer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Configurar o número de tentativas de reenvio
        properties.put("retries", 3);

        // Configurar o tempo de espera entre tentativas
        properties.put("retry.backoff.ms", 100);

        // Habilitar idempotência para evitar mensagens duplicadas
        properties.put("enable.idempotence", true);

        // Configurar o tempo limite de entrega
        properties.put("delivery.timeout.ms", 120000); // 120 segundos

        return new KafkaProducer<>(properties);
    }
}
```

Neste exemplo:
- `retries=3` especifica que o produtor tentará reenviar até 3 vezes em caso de erros.
- `retry.backoff.ms=100` define um atraso de 100ms entre as tentativas.
- `enable.idempotence=true` garante que as tentativas de reenvio não resultem em mensagens duplicadas.
- `delivery.timeout.ms=120000` define um tempo limite de 120 segundos para a entrega da mensagem.

### Configuração de `acks` no Kafka Producer

A configuração `acks` no Kafka Producer define o nível de confirmação que o produtor espera do broker antes de considerar uma mensagem como enviada com sucesso. Existem três configurações principais:

1. **`acks=0`**: O produtor não espera por nenhuma confirmação do broker. A mensagem é considerada enviada assim que é escrita na rede. Não há garantia de que a mensagem foi recebida com sucesso pelo broker.
2. **`acks=1`**: O produtor espera pela confirmação do broker líder. A mensagem é considerada enviada com sucesso assim que o broker líder a confirma. Não há garantia de que a mensagem foi replicada para outros brokers.
3. **`acks=all` (ou `acks=-1`)**: O produtor espera pela confirmação do broker líder e de todas as réplicas em sincronia. A mensagem é considerada enviada com sucesso assim que todas as réplicas a confirmam. Isso proporciona o maior nível de durabilidade.

### Exemplo de Configuração de `acks` no Kafka Producer

Aqui está um exemplo de como configurar a propriedade `acks` em um produtor Kafka:

```java
import org.apache.kafka.clients.producer.KafkaProducer;
import java.util.Properties;

public class KafkaProducerConfig {

    public static KafkaProducer<String, String> createProducer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Configurar o nível de confirmação
        properties.put("acks", "all"); // Pode ser "0", "1" ou "all"

        return new KafkaProducer<>(properties);
    }
}
```

Neste exemplo:
- `acks=all` garante que a mensagem só será considerada enviada com sucesso após a confirmação do broker líder e de todas as réplicas.

