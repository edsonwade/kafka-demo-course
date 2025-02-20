# Kafka Consumer Demo

Este projeto demonstra como criar um consumidor Kafka em Java e ler mensagens de um tópico Kafka.

## Pré-requisitos

- Java 11 ou superior
- Apache Kafka
- Maven

## Configuração

1. **Instalar o Apache Kafka**: Siga o guia [Kafka Quickstart](https://kafka.apache.org/quickstart) para instalar e executar o Kafka na sua máquina local.

2. **Criar um Tópico Kafka**: Abra um terminal e crie um tópico chamado `demo_java` com o seguinte comando:
    ```bash
    kafka-topics --create --topic demo_java --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
    ```

## Executando o Consumidor

1. **Clonar o repositório**:
    ```bash
    git clone <repository-url>
    cd <repository-directory>
    ```

2. **Construir o projeto**:
    ```bash
    mvn clean install
    ```

3. **Executar o consumidor**:
    ```bash
    mvn exec:java -Dexec.mainClass="code.with.vanilson.consumidor.ConsumerDemo"
    ```

## Verificando a Entrega das Mensagens

Para verificar se as mensagens foram lidas com sucesso, você pode usar o console consumidor do Kafka:

```bash
kafka-console-consumer --bootstrap-server localhost:9092 --topic demo_java --from-beginning
```

## Configuração
As propriedades do consumidor são configuradas na classe ```ConsumerDemo```. Aqui estão as principais configurações:  
- **bootstrap.servers**: O endereço do broker Kafka.
- **group.id**: O identificador do grupo de consumidores.
- **auto.offset.reset**: define o comportamento de recuperação de offset.
- **key.deserialize**r: A classe de desserialização da chave.
- **value.deserializer**: A classe de desserialização do valor.

## Visão Geral do Código

O projeto consiste nos seguintes arquivos:

- **ConsumerDemo.java**: classe principal que cria um consumidor Kafka e lê mensagens de um tópico.
- **KafkaConfig.java**: classe de configuração que define as propriedades do consumidor.
- **MessageListener.java**: interface funcional que define um método para processar mensagens recebidas.
- **MessageProcessor.java**: implementação de MessageListener que imprime as mensagens recebidas.
- **log4j2.xml**: arquivo de configuração do Log4j2 para definir o nível de log e o layout de saída.
- **pom.xml**: arquivo de configuração do Maven que define as dependências do projeto.
- **README.md**: documentação do projeto.
- **.gitignore**: arquivo que especifica os arquivos e diretórios a serem ignorados pelo Git.

### Consumidores grupos
- Os consumidores Kafka são agrupados em grupos de consumidores para compartilhar a carga de leitura de um ou mais tópicos.
- Cada grupo de consumidores tem um identificador exclusivo chamado group.id. 
- Os consumidores num grupo compartilham o trabalho de ler mensagens de um ou mais tópicos.
- Cada mensagem num tópico é entregue a apenas um consumidor em cada grupo de consumidores.
- Se houver mais consumidores do que partições num tópico, alguns consumidores ficarão inativos.
- Se houver mais partições do que consumidores num grupo, algumas partições não serão lidas.
- O comando kafka-consumer-groups é usado para listar, redefinir e excluir grupos de consumidores.
- O comando kafka-consumer-groups --list lista todos os grupos de consumidores ativos.
- O comando kafka-consumer-groups --describe exibe informações detalhadas sobre um grupo de consumidores.
- O comando kafka-consumer-groups --reset-offsets redefinirá os offsets de um grupo de consumidores para um valor específico.
- O comando kafka-consumer-groups --delete excluirá um grupo de consumidores.
- O comando kafka-consumer-groups --bootstrap-server localhost:9092 --list lista todos os grupos de consumidores ativos.
- O comando kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group my-group exibe informações detalhadas sobre um grupo de consumidores.
- O comando kafka-consumer-groups --bootstrap-server localhost:9092 --reset-offsets --group my-group --topic demo_java --to-earliest --execute redefinirá os offsets de um grupo de consumidores para o início do tópico.
- O comando kafka-consumer-groups --bootstrap-server localhost:9092 --delete --group my-group excluirá um grupo de consumidores.

### Consumidores Offsets
- O offset de um consumidor é a posição de leitura atual de um tópico.
- O offset é um número inteiro que identifica a posição de uma mensagem num tópico.
- O offset é armazenado no Kafka para rastrear o progresso de um consumidor.
- O offset é gerenciado pelo consumidor e pode ser controlado manual ou automaticamente.
- O offset é redefinido para o início, o fim ou um valor específico de uma partição.
- O offset é redefinido para o início do tópico com o comando kafka-consumer-groups --reset-offsets.
- O offset é redefinido para o início do tópico com o comando kafka-consumer-groups --reset-offsets --to-earliest.
- O offset é redefinido para o fim do tópico com o comando kafka-consumer-groups --reset-offsets --to-latest.
- O offset é redefinido para um valor específico com o comando kafka-consumer-groups --reset-offsets --to-offset.
- O offset é redefinido para um valor específico com o comando kafka-consumer-groups --reset-offsets --to-offset 100.
- O offset é redefinido para um valor específico com o comando kafka-consumer-groups --reset-offsets --to-offset 100 --execute.
- O offset é redefinido para um valor específico com o comando kafka-consumer-groups --reset-offsets --group my-group --topic demo_java --to-offset 100 --execute.

### Consumidores Deserialização
- A desserialização é o processo de converter bytes em objetos.
- A desserialização é usada para converter a chave e o valor de uma mensagem Kafka em objetos Java.
- A desserialização é configurada com as propriedades key.deserializer e value.deserializer.
- A desserialização é configurada com as classes de desserialização StringDeserializer para a chave e o valor.
### Consumidores Tratamento de Erros
- O tratamento de erros é essencial para lidar com exceções e falhas durante a leitura de mensagens.
- O tratamento de erros é feito com blocos try-catch para capturar exceções.
- O tratamento de erros é feito com o método poll que lança exceções, exceções de interrupção e exceções de tempo limite.

### Consumidores Rebalanceamento
- O rebalanceamento de consumidores ocorre quando um consumidor é adicionado ou removido de um grupo de consumidores.
- Durante o rebalanceamento, as partições do tópico são redistribuídas entre os consumidores ativos.
- O rebalanceamento é automático e transparente para os consumidores.
- O rebalanceamento é acionado quando um consumidor é adicionado ou removido de um grupo de consumidores.
- O rebalanceamento é acionado quando um consumidor falha ou é reiniciado.

### Consumidores subscritores
- O método subscribe é usado para inscrever um consumidor num ou mais tópicos.
- O método subscribe aceita uma lista de tópicos para inscrever o consumidor.
- O método subscribe aceita um objeto de retorno de chamada para processar mensagens recebidas, erros, exceções e o rebalanceamento do consumidor

### none ,earliest, latest
- O auto.offset.reset é usado para definir o comportamento de recuperação de offset.
- O auto.offset.reset é definido como none, earliest ou latest.
- O auto.offset.reset é definido como none para lançar uma exceção se não houver offset.
- O auto.offset.reset é definido como earliest para ler do início do tópico se não houver offset.
- O auto.offset.reset é definido como latest para ler do final do tópico se não houver offset.



### Consumidores Polling
- O método poll é usado para buscar novas mensagens do Kafka.
- O método poll aceita um argumento de duração que especifica o tempo máximo de espera por novas mensagens.
- O método poll retorna um conjunto de registros de consumidor que contém as mensagens recebidas.
- O método poll é bloqueante e aguarda até que novas mensagens estejam disponíveis ou o tempo limite seja atingido.


- **ConsumerDemo.java**
```java
package code.with.vanilson.consumidor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerDemo {
    private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class.getName());

    public static void main(String[] args) {
        Properties props = getProperties();
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList("demo_java"));
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    log.info("Polling for new messages");
                    var records = consumer.poll(Duration.ofMillis(1000));
                    for (ConsumerRecord<String, String> record : records) {
                        log.info("Key: {}, Value: {}", record.key(), record.value());
                        log.info("Partition: {}, Offset: {}", record.partition(), record.offset());
                    }
                } catch (Exception e) {
                    log.error("Error while consuming message from Kafka {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "my-group");
        props.setProperty("auto.offset.reset", "earliest");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }
}
```
## Explicação do codigo.
- Para criar um consumidor Kafka em Java, você precisa instanciar um objeto KafkaConsumer com as propriedades de configuração apropriadas.
- O método main inicia o consumidor, inscreve-se no tópico demo_java e começa a consumir mensagens do Kafka.
- O loop while verifica continuamente se há novas mensagens no tópico e as processa.
- O método getProperties define as propriedades do consumidor, incluindo o endereço do broker Kafka, o identificador do grupo de consumidores, o comportamento de recuperação de offset e as classes de desserialização para a chave e o valor.

### Comandos Adicionais do Kafka
1. Resetar Offsets
```bash
kafka-consumer-groups --reset-offsets --group my-group --topic demo_java --to-earliest --execute --bootstrap-server localhost:9092
```
2. Deletar Grupo de Consumidores
```bash
kafka-consumer-groups --delete --group my-group --bootstrap-server localhost:9092
```
## Contribuição
As contribuições são o que tornam a comunidade de código aberto um lugar incrível para aprender, inspirar e criar. Quaisquer contribuições que você fizer são **muito apreciadas**.

## Referências
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Kafka Quickstart](https://kafka.apache.org/quickstart)
- [Kafka Consumer API](https://kafka.apache.org/documentation/#consumerapi)
- [Kafka Consumer Configurations](https://kafka.apache.org/documentation/#newconsumerconfigs)
- [Kafka Consumer Groups](https://kafka.apache.org/documentation/#intro_consumergroups)
- [Kafka Consumer Offset Management](https://kafka.apache.org/documentation/#offsets)
- [Kafka Consumer Rebalancing](https://kafka.apache.org/documentation/#rebalance)
- [Kafka Consumer Polling](https://kafka.apache.org/documentation/#polling)
- [Kafka Consumer Deserialization](https://kafka.apache.org/documentation/#consumerconfigs_deserializer)
- [Kafka Consumer Error Handling](https://kafka.apache.org/documentation/#consumerconfigs_errorhandling)
- [Kafka Consumer Performance Tuning](https://kafka.apache.org/documentation/#consumerconfigs_performance)
- [Kafka Consumer Security](https://kafka.apache.org/documentation/#security)
- [Kafka Consumer Best Practices](https://kafka.apache.org/documentation/#consumer_bestpractices)
- [Kafka Consumer Examples](https://kafka.apache.org/documentation/#consumer_examples)
- [Kafka Consumer Javadoc](https://kafka.apache.org/javadoc/current/org/apache/kafka/clients/consumer/KafkaConsumer.html)
- [Kafka Consumer Tutorial](https://kafka.apache.org/documentation/#consumer_tutorial)
- [Kafka Consumer Performance](https://kafka.apache.org/documentation/#consumer_performance)

## Licença
Este projeto está licenciado sob a licença MIT - consulte o arquivo [LICENSE](LICENSE) para obter detalhes.
