### Resumo do Código

O código `OpenSearchConsumer.java` é um consumidor Kafka que lê mensagens de um tópico Kafka (`wikimedia-recentchange`) e as envia para um ‘cluster’ OpenSearch. O consumidor verifica se o índice `wikimedia` já existe no OpenSearch e, se não existir, cria-o. Em seguida, ele consome mensagens do tópico Kafka e as envia para o OpenSearch, garantindo que cada mensagem seja enviada apenas uma vez (idempotência).

### Alteração Feita

Para tornar o consumidor idempotente, foi implementada uma verificação de idempotência. Isso é feito extraindo um ID único de cada mensagem Kafka e usando esse ID ao indexar a mensagem no OpenSearch. Se uma mensagem com o mesmo ID já foi indexada, ela não será indexada novamente.

### README em Português

```markdown
# Consumidor OpenSearch Idempotente

Este projeto contém um consumidor Kafka que lê mensagens de um tópico Kafka e as envia para um cluster OpenSearch. O consumidor é idempotente, garantindo que cada mensagem seja enviada para o OpenSearch apenas uma vez.

## Como Funciona

1. **Criação do Cliente OpenSearch**: O consumidor cria um cliente OpenSearch para se conectar ao cluster.
2. **Verificação do Índice**: O consumidor verifica se o índice `wikimedia` já existe no OpenSearch. Se não existir, ele cria o índice.
3. **Consumo de Mensagens**: O consumidor se inscreve no tópico Kafka `wikimedia-recentchange` e começa a consumir mensagens.
4. **Idempotência**: Para garantir que cada mensagem seja enviada apenas uma vez, o consumidor extrai um ID único de cada mensagem e usa esse ID ao indexar a mensagem no OpenSearch. Se uma mensagem com o mesmo ID já foi indexada, ela não será indexada novamente.

## Exemplo de Código

```java
// Método principal do consumidor
public static void main(String[] args) {
    try (RestHighLevelClient openSearchClient = OpenSearchClientConsumer.createOpenSearchClient();
         KafkaConsumer<String, String> consumer = KafkaConsumerClient.createKafkaConsumer()) {
        
        // Verifica se o índice existe
        boolean indexExists = openSearchClient.indices().exists(new GetIndexRequest(WIKIMEDIA), RequestOptions.DEFAULT);
        if (!indexExists) {
            CreateIndexRequest request = new CreateIndexRequest(WIKIMEDIA);
            openSearchClient.indices().create(request, RequestOptions.DEFAULT);
            log.info("O índice wikimedia foi criado.");
        } else {
            log.info("O índice wikimedia já existe.");
        }

        // Inscreve-se no tópico Kafka
        consumer.subscribe(Collections.singleton("wikimedia-recentchange"));

        // Consome mensagens
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
            int recordCount = records.count();
            log.info("Recebidos {} registros", recordCount);

            if (recordCount == 0) {
                continue;
            }

            // Envia as mensagens para o OpenSearch
            for (var record : records) {
                indexRecord(record, openSearchClient);
            }
        }
    } catch (IOException e) {
        log.error("Ocorreu um erro: {}", e.getMessage());
    }
}

// Método para indexar registros no OpenSearch
private static void indexRecord(ConsumerRecord<String, String> record, RestHighLevelClient openSearchClient) throws IOException {
    try {
        String id = extractIdFromJsonValue(record.value());
        IndexRequest indexRequest = new IndexRequest(WIKIMEDIA)
                .source(record.value(), XContentType.JSON)
                .id(id);

        var indexResponse = openSearchClient.index(indexRequest, RequestOptions.DEFAULT);
        log.info("Registro indexado com ID: {}", indexResponse.getId());
    } catch (OpenSearchStatusException e) {
        log.error("Erro ao enviar dados para o OpenSearch: {}", e.getMessage());
    }
}

// Método para extrair o ID do valor JSON
private static String extractIdFromJsonValue(String json) {
    return JsonParser.parseString(json)
            .getAsJsonObject()
            .get("meta")
            .getAsJsonObject()
            .get("id")
            .getAsString();
}
```

## Como Executar

1. Certifique-se de que o Kafka e o OpenSearch estão em execução.
2. Compile e execute o projeto.
3. O consumidor começará a consumir mensagens do tópico Kafka `wikimedia-recentchange` e enviá-las para o OpenSearch, garantindo que cada mensagem seja enviada apenas uma vez.

## Requisitos

- Java 11 ou superior
- Apache Kafka
- OpenSearch

## Contribuição

Sinta-se à vontade para contribuir com melhorias para este projeto. Faça um fork do repositório, crie um branch para suas alterações e envie um pull request.


to check the status of the Docker service without using `systemctl`, you can use the following command:

```bash
sudo service docker status
```


## Licença

Este projeto está licenciado sob a Licença MIT. Consulte o arquivo LICENSE para obter mais informações.
```