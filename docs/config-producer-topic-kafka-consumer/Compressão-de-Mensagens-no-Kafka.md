### Compressão de Mensagens no Kafka

#### Explicação
A compressão de mensagens no Kafka pode ser configurada no nível do produtor, tópico ou broker. A compressão ajuda a reduzir o tamanho dos dados transmitidos e armazenados, melhorando a eficiência da rede e o desempenho do armazenamento.

#### Tipos de Compressão
Os tipos de compressão suportados pelo Kafka são:
- **none**: Sem compressão.
- **gzip**: Bom nível de compressão, mas pode ser mais lento.
- **snappy**: Rápido, mas com menor taxa de compressão.
- **lz4**: equilíbrio entre velocidade e taxa de compressão.
- **zstd**: Alta taxa de compressão e velocidade, disponível a partir do Kafka 2.1.

#### Configuração de Compressão no Produtor
Para configurar a compressão no produtor, você pode definir a propriedade `compression.type`:

```java
public static void main(String[] args) {

    Properties properties = new Properties();
    properties.setProperty("bootstrap.servers", "localhost:9092");
    properties.setProperty("key.serializer", StringSerializer.class.getName());
    properties.setProperty("value.serializer", StringSerializer.class.getName());
    properties.setProperty("compression.type", "gzip"); // Tipo de compressão
    KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
}
```

#### Configuração de Compressão no Tópico
A compressão no nível do tópico pode ser configurada ao criar o tópico:

```sh
kafka-topics.sh --create --topic my_topic --partitions 3 --replication-factor 1 --config compression.type=gzip --bootstrap-server localhost:9092
```

#### Configuração de Compressão no Broker
Para configurar a compressão no nível do broker, adicione a seguinte linha no arquivo `server.properties`:

```properties
compression.type=gzip
```

#### Batch de Mensagens
O Kafka agrupa mensagens em batches para melhorar a eficiência. A compressão é aplicada ao batch inteiro, não a cada mensagem individualmente. Isso maximiza a eficiência da compressão.

#### Vantagens da Compressão
- **Redução do uso de rede**: Menos dados são transmitidos pela rede.
- **Melhor desempenho de armazenamento**: Menos espaço é necessário para armazenar os dados.
- **Maior throughput**: A compressão pode aumentar a taxa de transferência de dados.

#### Desvantagens da Compressão
- **Sobrecarga de CPU**: A compressão e descompressão consomem recursos de CPU.
- **Latência**: Pode aumentar a latência devido ao tempo necessário para compressão e descompressão.

#### Uso de Compressão
- **Quando usar**: Em cenários onde a largura de banda da rede é limitada ou o armazenamento é caro.
- **Quando evitar**: Em sistemas com restrições de CPU ou onde a latência é crítica.

### Resumo
A compressão de mensagens no Kafka pode ser configurada no produtor, tópico ou broker. Os tipos de compressão incluem `none`, `gzip`, `snappy`, `lz4` e `zstd`. A compressão reduz o uso de rede e armazenamento, mas pode aumentar a sobrecarga de CPU e a latência. É recomendada em ambientes com limitações de largura de banda ou armazenamento, mas deve ser usada com cautela em sistemas sensíveis à latência.