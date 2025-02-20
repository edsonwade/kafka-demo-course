## Auto Offset Commit em Consumidores Kafka

### O que é Auto Offset Commit?

Auto offset commit é uma funcionalidade nos consumidores Kafka que automaticamente comita os offsets das mensagens que foram processadas. Isso significa que o consumidor periodicamente registra a posição da última mensagem processada, para que, se o consumidor reiniciar, ele possa retomar a partir do último offset comitado.

### Como Habilitar Auto Offset Commit

Para habilitar o auto offset commit, você precisa definir a propriedade `enable.auto.commit` como `true` na configuração do consumidor. Além disso, você pode configurar a frequência dos commits usando a propriedade `auto.commit.interval.ms`.

### Exemplo de Configuração

Aqui está um exemplo de como configurar um consumidor Kafka com auto offset commit habilitado:

```java
public static void main(String[] args) {

    Properties props = new Properties();
    props.setProperty("bootstrap.servers", "localhost:9092");
    props.setProperty("group.id", "my-group");
    props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("enable.auto.commit", "true");
    props.setProperty("auto.commit.interval.ms", "1000");

    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Collections.singletonList("demo_java"));
}
```
}

### Benefícios do Auto Offset Commit

1. **Simplicidade**: Reduz a complexidade de gerir offsets manualmente.
2. **Recuperação Automática**: Garante que o consumidor possa retomar a partir do último offset comitado após uma reinicialização.

### Desvantagens do Auto Offset Commit

1. **Potencial Perda de Dados**: Se o consumidor falhar antes do offset ser comitado, algumas mensagens podem ser reprocessadas.
2. **Falta de Controle**: Menos controle sobre quando os offsets são comitados, o que pode ser problemático em cenários que requerem gestão preciso de offsets.

### Conclusão

Auto offset commit é uma funcionalidade útil para simplificar o gerenciamento de offsets em consumidor Kafka. No entanto, é importante entender as suas limitações e considerar se o gerenciamento manual de offsets pode ser mais apropriado para o seu caso de uso.


### Sincronização e Assincronização em Consumidores Kafka

### Sincronização

Na sincronização, o consumidor Kafka processa as mensagens e comita os offsets de forma síncrona. Isso significa que o consumidor espera que o commit seja concluído antes de continuar a processar a próxima mensagem. Esse método garante que os offsets sejam comitados de forma consistente, mas pode ser mais lento devido ao tempo de espera.

#### Exemplo de Commit Síncrono

```java
public static void main(String[] args) {

    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
        consumer.subscribe(Collections.singletonList("demo_java"));
        while (true) {
            var records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                // Processa a mensagem
                System.out.printf("Key: %s, Value: %s%n", record.key(), record.value());
                System.out.printf("Partition: %d, Offset: %d%n", record.partition(), record.offset());
            }
            // Commit síncrono
            consumer.commitSync();
        }
    }
}
```

### Assincronização

Na assincronização, o consumidor Kafka processa as mensagens e comita os offsets de forma assíncrona. Isso significa que o consumidor não espera que o commit seja concluído antes de continuar a processar a próxima mensagem. Esse método pode ser mais rápido, mas há um risco de inconsistência se o commit falhar.

#### Exemplo de Commit Assíncrono

```java
public static void main(String[] args) {
    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
        consumer.subscribe(Collections.singletonList("demo_java"));
        while (true) {
            var records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                // Processa a mensagem
                System.out.printf("Key: %s, Value: %s%n", record.key(), record.value());
                System.out.printf("Partition: %d, Offset: %d%n", record.partition(), record.offset());
            }
            // Commit assíncrono
            consumer.commitAsync((offsets, exception) -> {
                if (exception != null) {
                    System.err.printf("Commit failed for offsets %s%n", offsets, exception);
                }
            });
        }
    }
}

```

### Conclusão

A escolha entre commit síncrono e assíncrono depende do caso de uso específico. O commit síncrono oferece mais consistência, enquanto o commit assíncrono pode melhorar o desempenho.