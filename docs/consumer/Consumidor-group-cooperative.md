# Configuração do Particionador de Atribuição do Consumidor Kafka

## O que é?

A configuração do particionador de atribuição do consumidor Kafka determina como as partições dos tópicos são
distribuídas entre os consumidores de um grupo de consumidores. Esta configuração é crucial para garantir que as
mensagens sejam processadas de forma eficiente e equilibrada entre os consumidores.

## Por que é importante?

É importante configurar o particionador de atribuição do consumidor Kafka corretamente para garantir que as
mensagens sejam processadas de forma eficiente e equilibrada entre os consumidores. Se a configuração do particionador
de atribuição não for otimizada, alguns consumidores podem receber mais mensagens do que outros, o que pode levar a um
desequilíbrio na carga de trabalho e atrasos no processamento de mensagens.

## Como configurar o particionador de atribuição do consumidor Kafka?

//TODO: Adicionar instruções sobre como configurar o particionador de atribuição do consumidor Kafka.
Para configurar o particionador de atribuição do consumidor Kafka, siga os passos abaixo:

1. Abra o arquivo de propriedades do consumidor Kafka. No seu projeto, este arquivo é `PropertiesUtils.java`.

2. Adicione ou modifique a propriedade `partition.assignment.strategy` para definir o particionador de atribuição
   desejado. Por exemplo, para usar o `CooperativeStickyAssignor`, adicione a seguinte linha:

```bash
props.setProperty("partition.assignment.strategy", "org.apache.kafka.clients.consumer.CooperativeStickyAssignor");
```

## Para que serve?

A configuração do particionador de atribuição serve para:

1. **Distribuição Equilibrada**: Garantir que as partições dos tópicos sejam distribuídas de forma equilibrada entre os
   consumidores do grupo.
2. **Rebalanceamento**: Facilitar o processo de reequilíbrio quando novos consumidores entram ou saem do grupo, ou
   quando novas partições são adicionadas aos tópicos.
3. **Eficiência**: Melhorar a eficiência do processamento das mensagens, evitando que um único consumidor fique
   sobrecarregado.

### Em Consumidor

Cada consumidor num grupo de consumidores é responsável por processar uma ou mais partições de um tópico. A
configuração do particionador de atribuição determina como essas partições são atribuídas aos consumidores. Por exemplo,
o particionador `RangeAssignor` atribui partições com base em intervalos, enquanto o `RoundRobinAssignor` distribui as
partições de forma circular.

### Em Grupo

Quando um grupo de consumidores é formado, o Kafka coordena a atribuição das partições entre os consumidores do grupo. O
processo de reequilíbrio ocorre quando há mudanças no grupo, como a adição ou remoção de consumidores, ou a adição de
novas partições aos tópicos. Durante o reequilíbrio, o Kafka usa o particionador configurado para determinar a nova
atribuição de partições.

### Exemplo de Configuração

Aqui está um exemplo de configuração do particionador de atribuição usando o `CooperativeStickyAssignor`:

```java
public static void main(String[] args) {

    Properties props = new Properties();
    props.setProperty("bootstrap.servers", "localhost:9092");
    props.setProperty("group.id", "my-group");
    props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("partition.assignment.strategy", "org.apache.kafka.clients.consumer.CooperativeStickyAssignor");
}
```

Neste exemplo, o CooperativeStickyAssignor é usado para garantir que as partições sejam atribuídas de forma cooperativa
e estável, minimizando o número de reatribuições durante o reequilíbrio

## Conclusão

A configuração do particionador de atribuição é uma parte essencial da configuração de um consumidor Kafka, garantindo
uma distribuição equilibrada e eficiente das partições entre os consumidores de um grupo. Ao escolher o particionador
correto e ajustar as configurações conforme necessário, você pode garantir que as mensagens sejam processadas de forma
eficiente e equilibrada, evitando atraso e sobrecargas desnecessárias nos consumidores.

# Kafka Consumer Groups

## O que são Grupos de Consumidores?

Grupos de consumidores em Kafka são uma forma de organizar múltiplos consumidores que trabalham juntos para processar
dados de um ou mais tópicos. Cada consumidor num grupo lê dados de uma partição exclusiva, garantindo que as mensagens
sejam processadas uma vez por grupo.

## Benefícios dos Grupos de Consumidores

1. **Escalabilidade**: Permite distribuir a carga de trabalho entre vários consumidores.
2. **Tolerância a Falhas**: Se um consumidor falhar, outro consumidor no grupo pode assumir a partição.
3. **Processamento Paralelo**: Vários consumidores podem processar mensagens simultaneamente, aumentando a eficiência.

## Como Funciona?

### Atribuição de Partições

Quando um grupo de consumidores é formado, o Kafka coordena a atribuição das partições dos tópicos entre os consumidores
do grupo. O processo de reequilíbrio ocorre quando há mudanças no grupo, como a adição ou remoção de consumidores, ou a
adição de novas partições aos tópicos.

### Exemplo de Configuração

Aqui está um exemplo de configuração de um consumidor Kafka num grupo:

```java
import java.util.*;

@SuppressWarnings("all")
public static void main(String[] args) {
    Properties props = new Properties();
    props.setProperty("bootstrap.servers", "localhost:9092");
    props.setProperty("group.id", "my-group");
    props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("partition.assignment.strategy", "org.apache.kafka.clients.consumer.CooperativeStickyAssignor");

    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Collections.singletonList("demo_java"));
}

```

## Rebalanceamento

Durante o reequilíbrio, o Kafka usa o particionador configurado para determinar a nova atribuição de partições. Por
exemplo, o```CooperativeStickyAssignor``` minimiza o número de reatribuições durante o reequilíbrio.

## Exemplo de Log de Rebalanceamento

```bash
Assigned partitions:                       []
Current owned partitions:                  []
Added partitions (assigned - owned):       []
Revoked partitions (owned - assigned):     []

[main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-my-group-1, groupId=my-group] Notifying assignor about the new Assignment(partitions=[])
[main] INFO org.apache.kafka.clients.consumer.internals.ConsumerRebalanceListenerInvoker - [Consumer clientId=consumer-my-group-1, groupId=my-group] Adding newly assigned partitions: 
[main] INFO code.with.vanilson.consumidor.ConsumerDemoWithShutdown - Polling for new messages
```

## Conclusão
Grupos de consumidores são uma ferramenta poderosa em Kafka para escalar o processamento de mensagens e garantir a tolerância a falhas. Configurar corretamente os consumidores e entender o processo de reequilíbrio é crucial para aproveitar ao máximo essa funcionalidade