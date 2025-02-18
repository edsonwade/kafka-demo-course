## Produtor com Chaves

O produtor envia mensagens com chaves para garantir que mensagens com a mesma chave sejam enviadas para a mesma
partição.
Isso é feito usando o seguinte código:

```java
package code.with.vanilson;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {
    private static final Logger log = LoggerFactory.getLogger(ProducerDemoKeys.class.getName());

    public static void main(String[] args) {
        Properties properties = getProperties();
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i <= 10; i++) {
                    String topic = "demo_java";
                    String key = "truck_id " + i;
                    String value = "Hello world " + i;
                    ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
                    producer.send(record, (recordMetadata, e) -> {
                        if (e == null) {
                            log.info("key :{} | Partition:{} ", key, recordMetadata.partition());
                        } else {
                            log.error("Error while sending message to Kafka {} ", e.getMessage());
                        }
                    });
                }
                Thread.sleep(500);
                producer.flush();
            }
        } catch (InterruptedException e) {
            log.error("Thread exception error ", e);
            Thread.currentThread().interrupt();
        }
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        return properties;
    }
}
```

## Explicaçãa

- **Configuração das Propriedades**: As propriedades do produtor são configuradas para se conectar ao broker Kafka, e os
  serializadores de chave e valor são definidos como ```StringSerializer```.

- **Envio de Mensagens**: O produtor envia 10 mensagens para o tópico ```demo_java```. Cada mensagem tem uma
  chave (```truck_id i```) e um valor (```Hello world i```). A chave garante que mensagens com a mesma chave sejam
  enviadas para a mesma partição.

- **Callback**: Um callback é fornecido para lidar com o resultado do envio da mensagem. Se a mensagem for enviada com
  sucesso, o log exibirá a chave e a partição para a qual a mensagem foi enviada. Se houver um erro, o log exibirá uma
  mensagem de erro.

- **Flush**: O produtor é limpo após o envio de todas as mensagens.

- **Thread Sleep**: Uma pausa de 500 ms é adicionada entre os envios de mensagens para simular um intervalo de tempo
  entre as mensagens.

- **Tratamento de Exceções**: Qualquer exceção lançada durante o envio de mensagens é tratada e registrada no log.

- **Log**: O log é usado para registrar mensagens de sucesso e erro durante o envio de mensagens.

- **Encerramento do Produtor**: O produtor é fechado automaticamente após o bloco try-with-resources.

- **Propriedades**: O método ```getProperties()``` é usado para configurar as propriedades do produtor.

- **Partições**: O log exibirá a chave e a partição para a qual a mensagem foi enviada.

- **Chave**: A chave é usada para garantir que mensagens com a mesma chave sejam enviadas para a mesma partição.

## resumo do código

O código acima é um produtor que envia mensagens com chaves para garantir que mensagens com a mesma chave sejam enviadas
para a mesma partição.

### Particionamento Round-Robin

Se não especificar uma chave, o Kafka usará um particionamento round-robin para distribuir as mensagens entre as
partições.
Isso significa que as mensagens serão distribuídas uniformemente entre as partições disponíveis e não haverá garantia de
que
mensagens com a mesma chave sejam enviadas para a mesma partição. Isso pode resultar em mensagens fora de ordem se a
ordem
for importante para o processamento dos dados.

### Resumo
Quando a chave é nula, o Kafka usa o particionador ```Round-Robin``` para distribuir mensagens entre as partições. Isso não
garante um balanceamento perfeito de carga, especialmente em cenários de alta concorrência. Para garantir que mensagens
com a mesma chave sejam enviadas para a mesma partição, é necessário especificar uma chave ao enviar mensagens.

## Execução
```java
package code.with.vanilson;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {
    private static final Logger log = LoggerFactory.getLogger(ProducerDemoKeys.class.getName());

    public static void main(String[] args) {
        Properties properties = getProperties();
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i <= 10; i++) {
                    String topic = "demo_java";
                    String value = "Hello world " + i;
                    ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
                    producer.send(record, (recordMetadata, e) -> {
                        if (e == null) {
                            log.info("Topic :{} | Partition:{} | Offset:{} | Timestamp :{}", 
                                    recordMetadata.topic(),
                                    recordMetadata.partition(),
                                    recordMetadata.offset(),
                                    recordMetadata.timestamp());
                        } else {
                            log.error("Error while sending message to Kafka {} ", e.getMessage());
                        }
                    });
                }
                Thread.sleep(500);
                producer.flush();
            }
        } catch (InterruptedException e) {
            log.error("Thread exception error ", e);
            Thread.currentThread().interrupt();
        }
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        return properties;
    }
}
```
### explicação
- **Partições**: O log exibirá o tópico, a partição, o offset e o timestamp para a mensagem enviada.
- **Chave**: Sem chave, o Kafka usará o particionador ```Round-Robin``` para distribuir mensagens entre as partições.
- **codigo**: O código acima é um produtor que envia mensagens sem chaves, resultando em um particionamento
  ```Round-Robin``` das mensagens entre as partições.
- **Execução**: O código pode ser executado para enviar mensagens sem chaves para o tópico ```demo_java```.
- **Resultado**: O log exibirá o tópico, a partição, o offset e o timestamp para cada mensagem enviada.
- **Particionamento Round-Robin**: Sem chave, o Kafka usará um particionamento ```Round-Robin``` para distribuir as
  mensagens entre as partições.
- **Resumo**: Quando a chave é nula, o Kafka usa o particionador ```Round-Robin``` para distribuir mensagens entre as
  partições. Isso não garante um balanceamento perfeito de carga, especialmente em cenários de alta concorrência.

## Particionador Sticky
O particionador ```Sticky``` é uma técnica que garante que mensagens com a mesma chave sejam enviadas para a mesma
partição. Isso pode ser útil para garantir a ordem de mensagens relacionadas ou para agrupar mensagens com base num
critério específico.
Garante um melhor desempenho e eficiência no processamento de dados no Kafka, pois as mensagens relacionadas são
agrupadas na mesma partição.
Isto garante o balanceamento de carga e a ordem de mensagens relacionadas, o que pode ser útil para cenários em que a
ordem é importante para o processamento dos dados.

### Resumo
O particionador ```Sticky``` é uma técnica que garante que mensagens com a mesma chave sejam enviadas para a mesma
partição. Isso pode ser útil para garantir a ordem de mensagens relacionadas ou para agrupar mensagens com base num
critério específico. Ao especificar uma chave ao enviar mensagens, é possível controlar como as mensagens são
distribuídas entre as partições e garantir um processamento eficiente dos dados no Kafka.




## Conclusão
O particionamento de chaves é uma técnica importante para garantir que mensagens com a mesma chave sejam enviadas para a
mesma partição. Isso pode ser útil para garantir a ordem de mensagens relacionadas ou para agrupar mensagens com base em
um critério específico. Ao especificar uma chave ao enviar mensagens, é possível controlar como as mensagens são
distribuídas entre as partições e garantir um processamento eficiente dos dados no Kafka.
