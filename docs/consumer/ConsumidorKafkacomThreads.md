## Consumidor Kafka com Threads

### Introdução

Em aplicações Kafka, é comum utilizar múltiplas threads para consumir mensagens de forma eficiente. Cada thread pode executar um consumidor Kafka, permitindo o processamento paralelo de mensagens.

### Configuração

Para configurar um consumidor Kafka em uma thread, você precisa criar uma classe que implemente `Runnable` ou estenda `Thread`. Dentro dessa classe, você configura e executa o consumidor Kafka.

### Exemplo de Implementação

#### Classe `ConsumerDemoThreads`

Esta classe demonstra como executar um consumidor Kafka em uma thread separada e lidar com o encerramento gracioso usando um gancho de encerramento.

```java
public class ConsumerDemoThreads {

    public static void main(String[] args) {
        ConsumerDemoWorker consumerDemoWorker = new ConsumerDemoWorker();
        new Thread(consumerDemoWorker).start();
        Runtime.getRuntime().addShutdownHook(new Thread(new ConsumerDemoCloser(consumerDemoWorker)));
    }

    private static class ConsumerDemoWorker implements Runnable {

        private static final Logger log = LoggerFactory.getLogger(ConsumerDemoWorker.class);

        private CountDownLatch countDownLatch;
        private Consumer<String, String> consumer;

        @Override
        public void run() {
            countDownLatch = new CountDownLatch(1);
            final Properties properties = new Properties();

            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "my-sixth-application");
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Collections.singleton("demo_java"));

            final Duration pollTimeout = Duration.ofMillis(100);

            try {
                while (true) {
                    final ConsumerRecords<String, String> consumerRecords = consumer.poll(pollTimeout);
                    for (final ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                        log.info("Getting consumer record key: '{}', value: '{}', partition: {}, offset: {} at {}",
                                consumerRecord.key(), consumerRecord.value(), consumerRecord.partition(),
                                consumerRecord.offset(), new Date(consumerRecord.timestamp()));
                    }
                }
            } catch (WakeupException e) {
                log.info("Consumer poll woke up");
            } finally {
                consumer.close();
                countDownLatch.countDown();
            }
        }

        /**
         * Para o consumidor Kafka e aguarda o término.
         * @throws InterruptedException - se ocorrer um erro ao aguardar o término.
         */
        void shutdown() throws InterruptedException {
            consumer.wakeup();
            countDownLatch.await();
            log.info("Consumer closed");
        }

    }

    private record ConsumerDemoCloser(ConsumerDemoWorker consumerDemoWorker) implements Runnable {
        private static final Logger log = LoggerFactory.getLogger(ConsumerDemoCloser.class);

        @Override
        public void run() {
            try {
                consumerDemoWorker.shutdown();
            } catch (InterruptedException e) {
                log.error("Error shutting down consumer", e);
            }
        }
    }
}

```

### Uso
Para usar as classes acima, você pode criar uma instância de ```ConsumerDemoWorker``` e iniciá-la em uma nova thread. A classe ```ConsumerDemoCloser``` garante que o consumidor seja encerrado de forma graciosa quando a aplicação for terminada.

### Para que serve

A classe `ConsumerDemoThreads` é usada para criar e gerenciar consumidores Kafka em threads separadas. Isso permite o processamento paralelo de mensagens, aumentando a eficiência e a capacidade de processamento da aplicação.

### Quando usar

Use a classe `ConsumerDemoThreads` quando você precisar consumir mensagens de um tópico Kafka de forma eficiente e paralela, especialmente em cenários onde o volume de mensagens é alto e o processamento sequencial não é suficiente.

### Vantagens

- **Processamento Paralelo**: Permite o processamento simultâneo de mensagens, melhorando a eficiência.
- **Escalabilidade**: Facilita a adição de mais consumidores conforme a demanda aumenta.
- **Isolamento**: Cada consumidor opera em sua própria thread, isolando falhas e melhorando a robustez.

### Desvantagens

- **Complexidade**: A implementação e o gerenciamento de múltiplas threads podem aumentar a complexidade do código.
- **Recursos**: O uso de múltiplas threads pode consumir mais recursos do sistema, como CPU e memória.
- **Sincronização**: Pode ser necessário lidar com problemas de sincronização e concorrência, como deadlocks e race conditions.

### Conclusão
Utilizar múltiplas threads para consumidores Kafka permite o processamento paralelo de mensagens, aumentando a eficiência e a capacidade de processamento da aplicação.