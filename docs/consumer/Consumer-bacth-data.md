O consumidor batch de dados é um padrão utilizado para processar grandes volumes de dados em lotes, ao invés de
processar cada mensagem individualmente. Esse padrão é comum em sistemas de mensageria como Apache Kafka, onde as
mensagens são consumidas em lotes para melhorar a eficiência e o desempenho.

### Vantagens do Consumidor Batch de Dados

1. **Eficiência**: Processar mensagens em lotes reduz a sobrecarga de operações repetitivas, como commits de offset e
   conexões de rede.
2. **Desempenho**: Aumenta o throughput do sistema, permitindo que mais mensagens sejam processadas em menos tempo.
3. **Consistência**: Garante que um conjunto de mensagens seja processado de forma atômica, o que pode ser importante
   para a integridade dos dados.

### Implementação em Java com Kafka

Abaixo está um exemplo de como implementar um consumidor batch de dados em Java utilizando Apache Kafka:

```java
package code.with.vanilson;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

@SuppressWarnings("all")
public class BatchDataConsumer {
    private static final Logger log = LoggerFactory.getLogger(BatchDataConsumer.class);

    public static void main(String[] args) {
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(KafkaConsumerConfig.getConsumerProps())) {
            consumer.subscribe(Collections.singletonList("your-topic"));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(1000L);
                if (!records.isEmpty()) {
                    for (ConsumerRecord<String, String> record : records) {
                        // Process each record
                        log.info("Consumed record with key {} and value {}", record.key(), record.value());
                    }
                    consumer.commitSync();
                    log.info("Offsets have been committed");
                } else {
                    log.info("Received 0 records");
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while consuming records", e);
        }
    }
}
```

### Explicação do Código

- **Configuração do Consumidor**: O consumidor é configurado com as propriedades necessárias para se conectar ao
  ‘cluster’ Kafka.
- **Assinatura do Tópico**: O consumidor se inscreve no tópico desejado.
- **Polling de Mensagens**: O método `poll` é utilizado para buscar mensagens do tópico em intervalos regulares.
- **Processamento de Mensagens**: As mensagens são processadas em um ‘loop’, e os offsets são comitados apenas após o
  processamento de todas as mensagens do lote.
- **Commit de Offsets**: O commit dos offsets é feito de forma síncrona após o processamento do lote, garantindo que as
  mensagens sejam marcadas como processadas apenas após o sucesso do processamento.

Esse padrão garante que o consumidor seja eficiente e os dados sejam processados de forma consistente e atômica.

### Segundo Exemplo

Implementação em Java com Kafka e OpenSearch usando BulkRequest
Abaixo está um exemplo de como implementar um consumidor batch de dados em Java utilizando Apache Kafka e OpenSearch,
utilizando BulkRequest para enviar os dados em lotes para o OpenSearch:
O consumidor batch de dados é um padrão utilizado para processar grandes volumes de dados em lotes, ao invés de
processar cada mensagem individualmente. Esse padrão é comum em sistemas de mensageria como Apache Kafka, onde as
mensagens são consumidas em lotes para melhorar a eficiência e o desempenho.

### Vantagens do Consumidor Batch de Dados

1. **Eficiência**: Processar mensagens em lotes reduz a sobrecarga de operações repetitivas, como commits de offset e
   conexões de rede.
2. **Desempenho**: Aumenta o throughput do sistema, permitindo que mais mensagens sejam processadas em menos tempo.
3. **Consistência**: Garante que um conjunto de mensagens seja processado de forma atômica, o que pode ser importante
   para a integridade dos dados.

### Implementação em Java com Kafka e OpenSearch usando BulkRequest

Abaixo está um exemplo de como implementar um consumidor batch de dados em Java utilizando Apache Kafka e OpenSearch,
utilizando `BulkRequest` para enviar os dados em lotes para o OpenSearch:

```java
package code.with.vanilson;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.bulk.BulkResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

@SuppressWarnings("all")
public class BatchDataConsumer {
    private static final Logger log = LoggerFactory.getLogger(BatchDataConsumer.class);

    public static void main(String[] args) {
        try (RestHighLevelClient openSearchClient = OpenSearchClientConsumer.createOpenSearchClient();
             KafkaConsumer<String, String> consumer = KafkaConsumerClient.createKafkaConsumer()) {

            consumer.subscribe(Collections.singletonList("your-topic"));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(1000L);
                if (!records.isEmpty()) {
                    BulkRequest bulkRequest = new BulkRequest();
                    for (ConsumerRecord<String, String> record : records) {
                        IndexRequest indexRequest = new IndexRequest("your-index")
                                .source(record.value(), XContentType.JSON);
                        bulkRequest.add(indexRequest);
                    }
                    if (bulkRequest.numberOfActions() > 0) {
                        BulkResponse bulkResponse = openSearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                        if (!bulkResponse.hasFailures()) {
                            consumer.commitSync();
                            log.info("Offsets have been committed");
                        } else {
                            log.error("Bulk request failed: {}", bulkResponse.buildFailureMessage());
                        }
                    }
                } else {
                    log.info("Received 0 records");
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while consuming records", e);
        }
    }
}
```

### Explicação do Código

- **Configuração do Consumidor**: O consumidor é configurado com as propriedades necessárias para se conectar ao
  ‘cluster’ Kafka.
- **Assinatura do Tópico**: O consumidor se inscreve no tópico desejado.
- **Polling de Mensagens**: O método `poll` é utilizado para buscar mensagens do tópico em intervalos regulares.
- **Processamento de Mensagens**: As mensagens são processadas em um ‘loop’, e os offsets são comitados apenas após o
  processamento de todas as mensagens do lote.
- **BulkRequest**: As mensagens são adicionadas a um `BulkRequest` para serem enviadas em lote ao OpenSearch.
- **Commit de Offsets**: O commit dos offsets é feito de forma síncrona após o processamento do lote, garantindo que as
  mensagens sejam marcadas como processadas apenas após o sucesso do processamento.

Esse padrão garante que o consumidor seja eficiente e os dados sejam processados de forma consistente e atômica.


O `BulkRequest` no consumidor é utilizado para agrupar várias operações de indexação em uma única solicitação para o OpenSearch. Isso melhora a eficiência e o desempenho ao reduzir o número de solicitações individuais enviadas ao servidor.

Aqui está um exemplo de como o `BulkRequest` é utilizado no consumidor:

```java
import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.opensearch.OpenSearchStatusException;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.bulk.BulkResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;

public class OpenSearchConsumer {
    private static final Logger log = LoggerFactory.getLogger(OpenSearchConsumer.class.getName());
    private static final String WIKIMEDIA_INDEX = "wikimedia";
    private static final String TOPIC = "wikimedia-recentchange";

    public static void main(String[] args) {
        try (RestHighLevelClient openSearchClient = OpenSearchClientConsumer.createOpenSearchClient();
             KafkaConsumer<String, String> consumer = KafkaConsumerClient.createKafkaConsumer()) {

            if (!openSearchClient.indices().exists(new GetIndexRequest(WIKIMEDIA_INDEX), RequestOptions.DEFAULT)) {
                CreateIndexRequest createIndexRequest = new CreateIndexRequest(WIKIMEDIA_INDEX);
                openSearchClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                log.info("Created index: {}", WIKIMEDIA_INDEX);
            } else {
                log.info("Index already exists: {}", WIKIMEDIA_INDEX);
            }

            consumer.subscribe(Collections.singleton(TOPIC));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
                int recordCount = records.count();
                log.info("Received {} records", recordCount);

                if (recordCount > 0) {
                    BulkRequest bulkRequest = new BulkRequest();
                    for (ConsumerRecord<String, String> record : records) {
                        addRecordToBulkRequest(record, bulkRequest);
                    }

                    if (bulkRequest.numberOfActions() > 0) {
                        BulkResponse bulkResponse = openSearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                        if (!bulkResponse.hasFailures()) {
                            log.info("Inserted: {} records", bulkResponse.getItems().length);
                        } else {
                            log.error("Bulk request failed: {}", bulkResponse.buildFailureMessage());
                        }
                    }

                    consumer.commitAsync((offsets, exception) -> {
                        if (exception == null) {
                            log.info("Offsets committed to Kafka");
                        } else {
                            log.error("Failed to commit offsets: {}", exception.getMessage());
                        }
                    });
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("Thread sleep interrupted: {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        } catch (IOException e) {
            log.error("An error occurred: {}", e.getMessage());
        }
    }

    private static void addRecordToBulkRequest(ConsumerRecord<String, String> record, BulkRequest bulkRequest) {
        try {
            String id = extractIdFromJsonValue(record.value());
            IndexRequest indexRequest = new IndexRequest(WIKIMEDIA_INDEX)
                    .source(record.value(), XContentType.JSON)
                    .id(id);
            bulkRequest.add(indexRequest);
        } catch (OpenSearchStatusException e) {
            log.error("An error occurred while adding record to bulk request: {}", e.getMessage());
        }
    }

    private static String extractIdFromJsonValue(String json) {
        return JsonParser.parseString(json)
                .getAsJsonObject()
                .get("meta")
                .getAsJsonObject()
                .get("id")
                .getAsString();
    }
}
```

Neste exemplo, o `BulkRequest` é criado e preenchido com várias operações de indexação. Após adicionar todas as operações, o `bulkRequest` é enviado ao OpenSearch. Se a solicitação for bem-sucedida, os registros são inseridos e os offsets são confirmados no Kafka.