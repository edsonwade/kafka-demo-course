### Procutor Acks

- **Productor Acks** é um mecanismo que permite que o produtor saiba se a mensagem foi entregue com sucesso ao broker.
- O produtor pode configurar o nível de garantia de entrega de mensagens usando a propriedade `acks`.
- Existem três valores possíveis para a propriedade `acks`:
    - `acks=0`: O produtor não aguarda confirmação de entrega da mensagem.
    - `acks=1`: O produtor aguarda a confirmação de entrega da mensagem pelo líder da partição.
    - `acks=all`: O produtor aguarda a confirmação de entrega da mensagem por todos os replicas da partição.
    - O valor padrão é `acks=1`.
    - O valor `acks=all` fornece a garantia mais forte de que a mensagem foi entregue com sucesso, mas também tem um
      impacto na latência de produção.
    - O valor `acks=0` fornece a menor garantia de entrega, mas também tem o menor impacto na latência de produção.

### Exemplo de Produtor com Acks

```java
package code.with.vanilson;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProductorDemoAcks {
    private static final Logger log = LoggerFactory.getLogger(ProductorDemoAcks.class.getName());

    public static void main(String[] args) {
        // Exemplo com acks=0
        Properties propertiesAcks0 = getProperties("0");
        sendMessages(propertiesAcks0);

        // Exemplo com acks=1
        Properties propertiesAcks1 = getProperties("1");
        sendMessages(propertiesAcks1);

        // Exemplo com acks=all
        Properties propertiesAcksAll = getProperties("all");
        sendMessages(propertiesAcksAll);
    }

    private static Properties getProperties(String acks) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        properties.setProperty("acks", acks);
        return properties;
    }

    private static void sendMessages(Properties properties) {
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            for (int i = 0; i < 10; i++) {
                String topic = "acks_topic";
                String key = "key_" + i;
                String value = "Hello world " + i;
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
                producer.send(record, (metadata, exception) -> {
                    if (exception == null) {
                        log.info("Sent message with key: {} to partition: {}, offset: {}", key, metadata.partition(),
                                metadata.offset());
                    } else {
                        log.error("Error while sending message: {}", exception.getMessage());
                    }
                });
            }
            producer.flush();
        }
    }
}
```

O código ```ProductorDemoAcks``` é um exemplo de um produtor Kafka que envia mensagens com diferentes configurações
de ```acks```. Aqui está um resumo do que ele faz e quando usar cada configuração:

### Resumo do Código

- **Configuração de acks**: O código configura o produtor Kafka para usar diferentes valores de acks (0, 1, all).
- **Envio de Mensagens**: Envia 10 mensagens para o tópico acks_topic com uma chave e um valor.
- **Callback**: Um callback é usado para registrar o sucesso ou falha do envio de cada mensagem.
- **Flush**: O produtor é limpo após o envio de todas as mensagens.

Quando a configuração ```acks``` é igual a ```0```, o produtor Kafka não espera por nenhum reconhecimento do broker.
Isso significa que o produtor considera a mensagem enviada assim que ela é escrita na rede, sem garantir que o broker a
tenha recebido

No log de saída, você vê que todas as mensagens são enviadas com um ```offset``` de ```-1```.
Isso ocorre porque o produtor não espera por uma confirmação do broker, então ele não sabe o ```offset``` real da
mensagem na partição.

- **Aqui está um resumo do que cada linha do log representa**:
    - **Sent message with key: key_0 to partition: 0, offset: -1**: A mensagem com a chave ```key_0``` foi enviada para
      a partição ```0``` com um offset de ```-1```.
    - **Sent message with key: key_1 to partition: 0, offset: -1**: A mensagem com a chave ```key_1``` foi enviada para
      a partição ```0``` com um offset de ```-1```.
    - **Sent message with key: key_2 to partition: 0, offset: -1**: A mensagem com a chave ```key_2``` foi enviada para
      a partição ```0``` com um offset de ```-1```.
    - **Sent message with key: key_3 to partition: 0, offset: -1**: A mensagem com a chave ```key_3``` foi enviada para
      a partição ```0``` com um offset de ```-1```.
    - **Sent message with key: key_4 to partition: 0, offset: -1**: A mensagem com a chave ```key_4``` foi enviada para
      a partição ```0``` com um offset de ```-1```.
    - **Sent message with key: key_5 to partition: 0, offset: -1**: A mensagem com a chave ```key_5``` foi enviada para
      a partição ```0``` com um offset de ```-1```.
    - **Sent message with key: key_6 to partition: 0, offset: -1**: A mensagem com a chave ```key_6``` foi enviada para
      a partição ```0``` com um offset de ```-1```.
    - **Sent message with key: key_7 to partition: 0, offset: -1**: A mensagem com a chave ```key_7``` foi enviada para
      a partição ```0``` com um offset de ```-1```.
    - **Sent message with key: key_8 to partition: 0, offset: -1**: A mensagem com a chave ```key_8``` foi enviada para
      a partição ```0``` com um offset de ```-1```.
    - **Sent message with key: key_9 to partition: 0, offset: -1**: A mensagem com a chave ```key_9``` foi enviada para
      a partição ```

- **kafka-producer-network-thread | producer-1**: Indica a thread do produtor Kafka que está enviando as mensagens.
- **INFO code.with.vanilson.ProductorDemoAcks**: Indica que a mensagem de log vem da classe ProductorDemoAcks.
- **Sent message with key: key_X to partition: Y, offset: -1**: Indica que a mensagem com a chave ```key_X``` foi
  enviada para a partição ```Y```, mas o ```offse```t é ```-1``` porque o produtor não recebeu confirmação do broker.
-

Este comportamento é esperado quando ```acks``` é configurado para ```0```, pois o produtor não espera por nenhum
reconhecimento e, portanto, não sabe o ```offset``` real das mensagens.

### Exemplo com acks=1

Quando a configuração ```acks``` é igual a ```1```, o produtor Kafka espera por uma confirmação do líder da partição.
Isso significa que o produtor considera a mensagem enviada assim que o líder da partição a recebe e confirma o
recebimento.

o log de saída, você vê que todas as mensagens são enviadas com um ```offset``` específico.
Isso ocorre porque o líder da partição reconhece a mensagem e retorna o ```offset``` real da mensagem na partição.

- **Aqui está um resumo do que cada linha do log representa**:
- **Sent message with key: key_0 to partition: 0, offset: 0**: A mensagem com a chave ```key_0``` foi enviada para a
  partição ```0``` com um offset de ```0```.
- **Sent message with key: key_1 to partition: 0, offset: 1**: A mensagem com a chave ```key_1``` foi enviada para a
  partição ```0``` com um offset de ```1```.
- **Sent message with key: key_2 to partition: 0, offset: 2**: A mensagem com a chave ```key_2``` foi enviada para a
  partição ```0``` com um offset de ```2```.
- **Sent message with key: key_3 to partition: 0, offset: 3**: A mensagem com a chave ```key_3``` foi enviada para a
  partição ```0``` com um offset de ```3```.
- **Sent message with key: key_4 to partition: 0, offset: 4**: A mensagem com a chave ```key_4``` foi enviada para a
  partição ```0``` com um offset de ```4```.
- **Sent message with key: key_5 to partition: 0, offset: 5**: A mensagem com a chave ```key_5``` foi enviada para a
  partição ```0``` com um offset de ```5```.
- **Sent message with key: key_6 to partition: 0, offset: 6**: A mensagem com a chave ```key_6``` foi enviada para a
  partição ```0``` com um offset de ```6```.
- **Sent message with key: key_7 to partition: 0, offset: 7**: A mensagem com a chave ```key_7``` foi enviada para a
  partição ```0``` com um offset de ```7```.
- **Sent message with key: key_8 to partition: 0, offset: 8**: A mensagem com a chave ```key_8``` foi enviada para a
  partição ```0``` com um offset de ```8```.
- **Sent message with key: key_9 to partition: 0, offset: 9**: A mensagem com a chave ```key_9``` foi enviada para a
  partição ```0``` com um offset de ```9```.

- **kafka-producer-network-thread | producer-1**: Indica a thread do produtor Kafka que envia as mensagens.
- **INFO code.with.vanilson.ProductorDemoAcks**: Indica que a mensagem de log vem da classe ProductorDemoAcks.
- **Sent message with key: key_X to partition: Y, offset: Z**: Indica que a mensagem com a chave ```key_X``` foi enviada
  para a partição ```Y``` e recebeu o ```offset``` Z após ser reconhecida pelo líder da partição.
  Este comportamento é esperado quando ```acks``` é configurado para ```1```, pois o produtor espera por um
  reconhecimento do líder da partição, garantindo que a mensagem foi recebida e registrada no log do líder.

### Exemplo com acks=all

Quando a configuração ```acks``` é igual a ```all```, o produtor Kafka espera por uma confirmação de todos os replicas
da partição.
Isso significa que o produtor considera a mensagem enviada assim que todos os replicas da partição a recebem e confirmam
o recebimento.

No log de saída, você vê que todas as mensagens são enviadas com um ```offset``` específico.
Isso ocorre porque todos os replicas da partição reconhecem a mensagem e retornam o ```offset``` real da mensagem na
partição.

- **Aqui está um resumo do que cada linha do log representa**:
- **Sent message with key: key_0 to partition: 0, offset: 0**: A mensagem com a chave ```key_0``` foi enviada para a
  partição ```0``` com um offset de ```0```.
- **Sent message with key: key_1 to partition: 0, offset: 1**: A mensagem com a chave ```key_1``` foi enviada para a
  partição ```0``` com um offset de ```1```.
- **Sent message with key: key_2 to partition: 0, offset: 2**: A mensagem com a chave ```key_2``` foi enviada para a
  partição ```0``` com um offset de ```2```.
- **Sent message with key: key_3 to partition: 0, offset: 3**: A mensagem com a chave ```key_3``` foi enviada para a
  partição ```0``` com um offset de ```3```.
- **Sent message with key: key_4 to partition: 0, offset: 4**: A mensagem com a chave ```key_4``` foi enviada para a
  partição ```0``` com um offset de ```4```.
- **Sent message with key: key_5 to partition: 1, offset: 5**: A mensagem com a chave ```key_5``` foi enviada para a
  partição ```1``` com um offset de ```5```.
- **Sent message with key: key_6 to partition: 1, offset: 6**: A mensagem com a chave ```key_6``` foi enviada para a
- **Sent message with key: key_7 to partition: 1, offset: 7**: A mensagem com a chave ```key_7``` foi enviada para a
- **Sent message with key: key_8 to partition: 1, offset: 8**: A mensagem com a chave ```key_8``` foi enviada para a
- **Sent message with key: key_9 to partition: 1, offset: 9**: A mensagem com a chave ```key_9``` foi enviada para a
- **Sent message with key: key_0 to partition: 0, offset: 10**: A mensagem com a chave ```key_0``` foi enviada para a
- **Sent message with key: key_1 to partition: 0, offset: 11**: A mensagem com a chave ```key_1``` foi enviada para a
- **Sent message with key: key_2 to partition: 0, offset: 12**: A mensagem com a chave ```key_2``` foi enviada para a
- **Sent message with key: key_3 to partition: 0, offset: 13**: A mensagem com a chave ```key_3``` foi enviada para a

- **kafka-producer-network-thread | producer-1**: Indica a thread do produtor Kafka que envia as mensagens.
- **INFO code.with.vanilson.ProductorDemoAcks**: Indica que a mensagem de log vem da classe ProductorDemoAcks.
- **Sent message with key: key_X to partition: Y, offset: Z**: Indica que a mensagem com a chave ```key_X``` foi enviada
  para a partição ```Y``` e recebeu o ```offset``` Z após ser reconhecida por todos os replicas da partição.
  Este comportamento é esperado quando ```acks``` é configurado para ```all```, pois o produtor espera por um
  reconhecimento de todos os replicas da partição, garantindo que a mensagem foi recebida e registrada no log de todos
  os replicas.

### Resumo

- **acks=0**: O produtor não aguarda confirmação de entrega da mensagem.
- **acks=1**: O produtor aguarda a confirmação de entrega da mensagem pelo líder da partição.
- **acks=all**: O produtor aguarda a confirmação de entrega da mensagem por todos os replicas da partição.
- **O valor padrão é acks=1**.
- **O valor acks=all fornece a garantia mais forte de que a mensagem foi entregue com sucesso, mas também tem um impacto
  na latência de produção**.
- **O valor acks=0 fornece a menor garantia de entrega, mas também tem o menor impacto na latência de produção**.
- **Dica**: Teste e ajuste a configuração de acks com base nos requisitos de latência e garantia de entrega do seu
  aplicativo.
- **Conclusão**: O mecanismo de acks do Kafka permite que os produtores ajustem a garantia de entrega de mensagens com
  base nas necessidades do aplicativo.
- **Use acks=0 para baixa latência e aceitação de perda de mensagens**.
- **Use acks=1 para equilíbrio entre latência e garantia de entrega**.
- **Use acks=all para garantia máxima de entrega, mesmo com maior latência**.


### Conclusão

O mecanismo de acks do Kafka permite que os produtores ajustem a garantia de entrega de mensagens com base nas
necessidades do aplicativo.

- Use acks=0 para baixa latência e aceitação de perda de mensagens.
- Use acks=1 para equilíbrio entre latência e garantia de entrega.
- Use acks=all para garantia máxima de entrega, mesmo com maior latência.