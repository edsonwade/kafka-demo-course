## Producer Default Partitioner & Sticky Partitioner in Apache Kafka

O particionador padrão do Kafka é responsável por determinar a partição para a qual uma mensagem será enviada. 
Existem dois particionadores principais usados pelo Kafka:

1. **Particionador Round-Robin**:
    - **Descrição**: Este particionador distribui as mensagens de forma equilibrada entre todas as partições disponíveis. Ele é usado quando a chave da mensagem é nula.
    - **Funcionamento**: Quando uma mensagem é enviada sem uma chave, o particionador Round-Robin seleciona a próxima partição disponível numa sequência circular, garantindo que todas as partições recebam aproximadamente o mesmo número de mensagens.
    - **Versão**: Este particionador é o padrão em versões anteriores ao Kafka 2.4.

2. **Particionador Sticky**:
    - **Descrição**: este particionador foi introduzido para melhorar o desempenho do produtor. Ele agrupa várias mensagens numa única partição até que um certo limite seja atingido, antes de mudar para outra partição.
    - **Funcionamento**: Quando uma mensagem é enviada sem uma chave, o particionador Sticky seleciona uma partição e continua a enviar mensagens para essa partição até que um limite de tamanho de lote ou tempo seja atingido. Isso reduz a sobrecarga de rede e melhora a eficiência.
    - **Versão**: este particionador tornou-se o padrão a partir do Kafka 2.4.

### Exemplo de Configuração do Particionador no Produtor

Aqui está um exemplo de como configurar o particionador no produtor Kafka:

```java
import org.apache.kafka.clients.producer.KafkaProducer;
import java.util.Properties;

public static void main(String[] args) {
   Properties properties = new Properties();
    properties.setProperty("bootstrap.servers", "localhost:9092");
    properties.setProperty("key.serializer", StringSerializer.class.getName());
    //continua
    properties.setProperty("partitioner.class", "org.apache.kafka.clients.producer.StickyPartitioner");
    KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
    //continua e não para
   
   
}
````
