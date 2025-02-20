A entrega ou semântica para consumidores em sistemas de mensagens como Kafka refere-se à garantia de como as mensagens
são entregues aos consumidores. Existem três tipos principais de semântica de entrega:

1. **At most once (No máximo uma vez)**:
    - **Descrição**: As mensagens são entregues no máximo uma vez. Pode haver perda de mensagens, mas nunca haverá
      duplicação.
    - **Vantagens**: Baixa latência, menos sobrecarga de processamento.
    - **Desvantagens**: Possibilidade de perda de mensagens.

2. **At least once (Pelo menos uma vez)**:
    - **Descrição**: As mensagens são entregues pelo menos uma vez. Pode haver duplicação de mensagens, mas nenhuma
      mensagem será perdida.
    - **Vantagens**: Garantia de que todas as mensagens serão processadas.
    - **Desvantagens**: Possibilidade de duplicação de mensagens, o que pode exigir lógica adicional para lidar com
      duplicatas.

3. **Exactly once (Exatamente uma vez)**:
    - **Descrição**: As mensagens são entregues exatamente uma vez. Não há perda nem duplicação de mensagens.
    - **Vantagens**: Garantia de processamento único e preciso de cada mensagem.
    - **Desvantagens**: Maior complexidade e sobrecarga de processamento, pode impactar a latência.

### Exemplos

#### At most once

```java
public static void main(String[] args) {

// Configuração do consumidor Kafka para "at most once"
    Properties props = new Properties();
    props.put("enable.auto.commit", "true");
    props.put("auto.commit.interval.ms", "1000");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
}
```

#### At least once

```java
@SuppressWarnings("all")
public static void main(String[] args) {

// Configuração do consumidor Kafka para "at least once"
    Properties props = new Properties();
    props.put("enable.auto.commit", "false");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

    while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
            // Processar a mensagem
        }
        consumer.commitSync(); // Commit manual após processamento
    }
}
```

#### Exactly once

```java
@SuppressWarnings("all")
public static void main(String[] args) {

// Configuração do consumidor Kafka para "exactly once"
    Properties props = new Properties();
    props.put("enable.idempotence", "true");
    props.put("isolation.level", "read_committed");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

    while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
            // Processar a mensagem
            long.class.cast(record.offset());
        }
        consumer.commitSync(); // Commit manual após processamento
    }
}
```

### Vantagens e Desvantagens

- **At most once**:
    - **Vantagens**: Simplicidade, baixa latência.
    - **Desvantagens**: Perda de mensagens.

- **At least once**:
    - **Vantagens**: Garantia de entrega.
    - **Desvantagens**: Possibilidade de duplicação, necessidade de lidar com duplicatas.

- **Exactly once**:
    - **Vantagens**: Processamento preciso e único.
    - **Desvantagens**: Maior complexidade, maior latência.