### Estratégia de Commit de Offset do Consumidor

No Kafka, o commit de offset é uma parte crucial do processo de consumo de mensagens. Ele garante que o consumidor saiba
até onde ele leu no tópico Kafka, permitindo que ele retome a leitura a partir do ponto correto em caso de falhas ou
reinicializações. Existem duas principais estratégias de commit de offset:

1. **Commit Automático (Auto-Commit)**:
    - O Kafka pode ser configurado para fazer o commit automático dos offsets em intervalos regulares.
    - Isso é controlado pela propriedade `enable.auto.commit` no consumidor Kafka.
    - Quando `enable.auto.commit` está definido como `true`, o consumidor faz o commit dos offsets automaticamente em
      intervalos definidos pela propriedade `auto.commit.interval.ms`.
    - Vantagem: Simplicidade, pois o Kafka gerencia os commits automaticamente.
    - Desvantagem: Pode levar a duplicação de mensagens em caso de falhas, pois os commits podem não refletir exatamente
      o ponto de processamento do consumidor.

2. **Commit Manual**:
    - O consumidor pode ser configurado para fazer o commit dos offsets manualmente.
    - Isso é feito definindo `enable.auto.commit` como `false` e chamando explicitamente o método `commitSync()`
      ou `commitAsync()` do consumidor.
    - `commitSync()`: Faz o commit de forma síncrona, garantindo que o commit foi bem-sucedido antes de prosseguir.
    - `commitAsync()`: Faz o commit de forma assíncrona, permitindo que o consumidor continue a processar mensagens
      enquanto o commit é realizado em segundo plano.
    - Vantagem: Maior controle sobre quando os offsets são comitados, reduzindo a hipótese de duplicação de mensagens.
    - Desvantagem: Requer mais código e gerenciamento por parte do desenvolvedor.

### Exemplo de Commit Manual

```java
public static void main(String[] args) {

    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
    consumer.subscribe(Collections.singletonList("wikimedia-recentchange"));

    try {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                // Processa a mensagem
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
            // Commit manual dos offsets
            consumer.commitSync();
        }
    } finally {
        consumer.close();
    }
}
```

Neste exemplo, o consumidor faz o commit dos offsets manualmente após processar cada lote de mensagens, garantindo que
os offsets reflitam com precisão o ponto de processamento.