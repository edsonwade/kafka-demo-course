# Documentação do Produtor Kafka

## Introdução

Este documento descreve como criar um produtor Kafka em Java, utilizando a
biblioteca `org.apache.kafka.clients.producer.KafkaProducer`. O produtor Kafka é responsável por enviar mensagens para
um tópico Kafka.

## Passo a Passo

### 1. Configuração das Propriedades do Produtor

Primeiro, configuramos as propriedades do produtor Kafka. As propriedades incluem o endereço do servidor
Kafka (`bootstrap.servers`), o serializador da chave (`key.serializer`) e o serializador do valor (`value.serializer`).

```java
private static Properties getProperties() {
    Properties properties = new Properties();
    properties.setProperty("bootstrap.servers", "localhost:9092");
    properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    return properties;
}
```

### 2. Criação do Produtor Kafka

Com as propriedades configuradas, criamos uma instância do KafkaProducer utilizando as propriedades definidas
anteriormente.

```bash
Properties properties = getProperties();
try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)){
        // Código para enviar mensagens
}catch (Exception e){
    e.printStackTrace();
}
```

### 3. Envio de Mensagens

Para enviar mensagens, utilizamos o método `send` do KafkaProducer. O método `send` recebe um objeto `ProducerRecord`
contendo o tópico, a chave e o valor da mensagem.
Enviamos uma mensagem para o tópico `my-topic` com a chave `1` e o valor `Hello, Kafka!`.
A mensagem é enviada de forma assíncrona, ou seja, o método `send` retorna imediatamente sem esperar a confirmação do
broker Kafka.
e composto por uma chave e um valor, ambos do tipo ‘String’.

```java
producer.send(new ProducerRecord<>("demo_java", "Nice works and it is working!"));
        log.info("Message sent successfully!");
```

### 4. Sincronização das Mensagens

Após enviar a mensagem, utilizamos o método flush para garantir que todas as mensagens pendentes sejam enviadas para o
tópico Kafka de forma síncrona (bloqueante).

```java
producer.flush();
```

### 5. Verificação do Envio da Mensagem

Para verificar se a mensagem foi enviada com sucesso, execute o seguinte comando no terminal:

```bash
kafka-console-consumer --bootstrap-server localhost:9092 --topic demo_java --from-beginning
```

### Obs:

Antes de enviar a mensagem, é necessário iniciar o servidor Kafka e criar o tópico `my-topic`:

```bash
kafka-topics --create --topic demo_java --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
```

Este comando irá consumir e exibir todas as mensagens do tópico demo_java desde o início, incluindo a mensagem enviada.

### Código Completo

```java
package code.with.vanilson;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {
    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getName());

    public static void main(String[] args) {
        Properties properties = getProperties();
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            producer.send(new ProducerRecord<>("demo_java", "Nice works and it is working!"));
            log.info("Message sent successfully!");
            producer.flush();
        } catch (Exception e) {
            log.error("Error while sending message to Kafka", e);
        }
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }
}
```

### Conclusão

Este documento descreveu como criar um produtor Kafka em Java utilizando a
biblioteca `org.apache.kafka.clients.producer.KafkaProducer`. O produtor Kafka é responsável por enviar mensagens para
um tópico Kafka. Para mais informações, consulte
a [documentação oficial do Apache Kafka](https://kafka.apache.org/documentation/).

### Comandos Kafka

- **Iniciar o servidor Kafka**: `bin/kafka-server-start.sh config/server.properties`

## Criar tópico Kafka com 1 partição e 1 réplica e enviar mensagens para o tópico Kafka usando um produtor Java.

```bash
kafka-topics --create --topic demo_java --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
```

### Consumir mensagens do tópico Kafka do início usando um consumidor Java.

```bash
kafka-console-consumer --bootstrap-server localhost:9092 --topic demo_java --from-beginning
```

### Enviar mensagens para o tópico Kafka usando um produtor Java e consumir mensagens do tópico Kafka usando um consumidor Java.

```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java
```

### Listar tópicos Kafka.
Listar todos os tópico Kafka disponíveis no ‘cluster’.
```bash
kafka-topics --list --bootstrap-server localhost:9092
```

### Apagar tópico Kafka e verificar se o tópico foi apagado.
Remvoer o tópico Kafka chamado demo_java.
```bash
kafka-topics --delete --topic demo_java --bootstrap-server localhost:9092
```

### Descrever tópico Kafka.
Descrição do tópico Kafka, incluindo o nome, número de partições, fator de replicação e configurações.
```bash
kafka-topics --describe --topic demo_java --bootstrap-server localhost:9092
```

### Alterar o número de partições do tópico Kafka.
Permite alterar o número de partições do tópico Kafka para 3.
```bash
kafka-topics --alter --topic demo_java --partitions 3 --bootstrap-server localhost:9092
```

### Alterar a retenção de mensagens do tópico Kafka.
Permite alterar a retenção de mensagens do tópico Kafka para 1 hora.
```bash
kafka-configs --alter --topic demo_java --add-config retention.ms=3600000 --bootstrap-server localhost:9092
```

### Criar grupo de consumidores Kafka.
Criar um grupo de consumidores Kafka chamado my-group.
```bash
kafka-consumer-groups --create --bootstrap-server localhost:9092 --group my-group --topic demo_java --reset-offsets --to-earliest --execute
```

### Listar grupos de consumidores Kafka.
Lista todos os grupos de consumidores Kafka.
```bash
kafka-consumer-groups --list --bootstrap-server localhost:9092
```

### Descrever grupo de consumidores Kafka.
Descreve um grupo de consumidores Kafka, incluindo o estado, o coordenador e os membros do grupo.
```bash
kafka-consumer-groups --describe --group my-group --bootstrap-server localhost:9092
```

### Resetar offsets do grupo de consumidores Kafka.
Permite redefinir os offsets do consumidor para o início do tópico.
```bash
kafka-consumer-groups --reset-offsets --group my-group --topic demo_java --to-earliest --execute --bootstrap-server localhost:9092
```

### Deletar grupo de consumidores Kafka.
Permite excluir um grupo de consumidor Kafka.
```bash
kafka-consumer-groups --delete --group my-group --bootstrap-server localhost:9092
```

### Escolher leader e seguidores do tópico Kafka.
Para escolher um líder e seguidores preferidos para um tópico Kafka, você pode usar o comando kafka-preferred-replica-election.
```bash
kafka-preferred-replica-election --bootstrap-server localhost:9092
```

### topics replication factor 3
Criar um tópico com um fator de replicação de 3 para garantir a tolerância a falhas.
```bash
kafka-topics --create --topic demo_java --bootstrap-server localhost:9092 --replication-factor 3 --partitions 1
```

### isp config
O que 'em-sync replicas' significa no Kafka?
O min.insync.replicas é uma configuração do tópico que especifica o número mínimo de réplicas que devem estar em sincronia para que um produtor possa considerar uma mensagem como confirmada. Se o número de réplicas em sincronia for menor que o min.insync.replicas, o produtor receberá um erro de NotEnoughReplicas após o envio da mensagem.


```bash
kafka-configs --alter --entity-type topics --entity-name demo_java --add-config min.insync.replicas=2 --bootstrap-server localhost:9092
```

### producer acks all
Permite garantir que a mensagem foi enviada para o líder e para todos os seguidores antes de retornar uma 
resposta de sucesso.

```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java --producer-property acks=all
```

### producer acks 1
Permite garantir que a mensagem foi enviada para o líder antes de retornar uma resposta de sucesso.

```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java --producer-property acks=1
```

### producer acks 0
Não garante que a mensagem foi enviada com sucesso.
```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java --producer-property acks=0
```

### Consumer atleast once
Para garantir que a mensagem seja consumida pelo menos uma vez, você pode usar o modo de isolamento read_committed.

```bash
kafka-console-consumer --bootstrap-server localhost:9092 --topic demo_java --from-beginning --group my-group
```

### Consumer exactly once
Para garantir que a mensagem seja consumida exatamente uma vez, você pode usar o modo de isolamento read_committed.

```bash
kafka-console-consumer --bootstrap-server localhost:9092 --topic demo_java --from-beginning --group my-group --isolation-level read_committed
```
### Consumer offset
Para redefinir os offsets do consumidor para o início do tópico, você pode usar o comando kafka-consumer-groups.
```bash
kafka-consumer-groups --reset-offsets --group my-group --topic demo_java --to-earliest --execute --bootstrap-server localhost:9092
```

### Round Robin Partitioner 
O RoundRobinPartitioner distribui as mensagens de forma equitativa entre as partições do tópico.
```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java --producer-property partitioner.class=org.apache.kafka.clients.producer.RoundRobinPartitioner
```

### Hash Partitioner 
O HashPartitioner distribui as mensagens com base no hash da chave da mensagem.
```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java --producer-property partitioner.class=org.apache.kafka.clients.producer.internals.DefaultPartitioner
```

### Custom Partitioner 
Você pode implementar um Partitioner personalizado para distribuir as mensagens com base em uma lógica personalizada.
```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java --producer-property partitioner.class=com.example.CustomPartitioner
```

### Producer Compression 
Para habilitar a compressão de mensagens, você pode usar a propriedade compression.type.
```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java --producer-property compression.type=gzip
```

### Producer Retries
Para reenviar mensagens automaticamente em caso de falha, você pode usar a propriedade retries.
```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java --producer-property retries=3
```

### Producer Batch Size
Para agrupar várias mensagens num único lote, você pode usar a propriedade batch.size.
```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java --producer-property batch.size=16384
```

### Producer Linger Ms 
Para aguardar um período antes de enviar um lote, você pode usar a propriedade linger.ms.
```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java --producer-property linger.ms=5
```

### Horizontal Scaling 
Para aumentar a capacidade de produção, você pode adicionar mais produtores ao grupo de produtores.
```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java
```
### Vertical Scaling
Para aumentar a capacidade de produção, você pode aumentar o tamanho dos produtores.
```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java --producer-property buffer.memory=33554432
```



### Referências

- [Documentação oficial do Apache Kafka](https://kafka.apache.org/documentation/)
- [KafkaProducer (JavaDocs)](https://kafka.apache.org/25/javadoc/org/apache/kafka/clients/producer/KafkaProducer.html)
- [ProducerRecord (JavaDocs)](https://kafka.apache.org/25/javadoc/org/apache/kafka/clients/producer/ProducerRecord.html)
- [Kafka Quickstart](https://kafka.apache.org/quickstart)
- [Kafka Tutorials](https://kafka.apache.org/documentation/#intro_tutorials)
- [Kafka Documentation](https://kafka.apache.org/documentation/)


