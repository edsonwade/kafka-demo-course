### Configuração de Propriedades no Kafka Producer

No Kafka 3.x, algumas propriedades vêm configuradas por padrão, como `min.insync.replicas`. Portanto, não é necessário configurá-las explicitamente, a menos que você queira alterar o valor padrão. Abaixo, explico as propriedades que você mencionou e quando configurá-las:

#### Propriedades Configuradas

1. **`enable.idempotence`**:
    - **Descrição**: Habilita a idempotência no produtor para evitar mensagens duplicadas.
    - **Quando Configurar**: Necessário configurar explicitamente em versões anteriores ao Kafka 3.x. No Kafka 3.x, já vem habilitado por padrão.
    - **Exemplo**:
      ```java
      public static void main(String[] args){
        Properties properties = new Properties();
        properties.setProperty("enable.idempotence", IS_BOOLEAN);
      }
      ```

2. **`retries`**:
    - **Descrição**: Define o número de tentativas de reenvio em caso de falhas transitórias.
    - **Quando Configurar**: Necessário configurar explicitamente em versões anteriores ao Kafka 3.x. No Kafka 3.x, já vem configurado por padrão.
    - **Exemplo**:
      ```java
        public static void main(String[] args){
            Properties properties = new Properties();
      properties.setProperty("retries", "3");
        }
      ```

3. **`retry.backoff.ms`**:
    - **Descrição**: Define o tempo de espera entre tentativas de reenvio.
    - **Quando Configurar**: Necessário configurar explicitamente em versões anteriores ao Kafka 3.x. No Kafka 3.x, já vem configurado por padrão.
    - **Exemplo**:
      ```java
       public static void main(String[] args){
        Properties properties = new Properties();
      properties.setProperty("retry.backoff.ms", "100");
      }
      ```

4. **`delivery.timeout.ms`**:
    - **Descrição**: Define o tempo máximo que o produtor tentará enviar uma mensagem antes de desistir.
    - **Quando Configurar**: Necessário configurar explicitamente em versões anteriores ao Kafka 3.x. No Kafka 3.x, já vem configurado por padrão.
    - **Exemplo**:
      ```java
      public static void main(String[] args){
        Properties properties = new Properties();
      properties.setProperty("delivery.timeout.ms", "120000"); // 2 minutos
      }
      ```

### Exemplo de Configuração para Versões Anteriores ao Kafka 3.x

```java
public static void main(String[] args) {

    Properties properties = new Properties();
    properties.setProperty("bootstrap.servers", "localhost:9092");
    properties.setProperty("key.serializer", StringSerializer.class.getName());
    properties.setProperty("value.serializer", StringSerializer.class.getName());

// Habilitar idempotência para evitar mensagens duplicadas
    properties.setProperty("enable.idempotence", IS_BOOLEAN);
// Configurar o número de tentativas de reenvio
    properties.setProperty("retries", "3");
// Configurar o tempo de espera entre tentativas
    properties.setProperty("retry.backoff.ms", "100");
// Configurar o tempo limite de entrega
    properties.setProperty("delivery.timeout.ms", "120000"); // 2 minutos
}
```

Para versões do Kafka 3.x e posteriores, essas configurações já vêm por padrão e não precisam ser explicitamente definidas, a menos que você queira alterar os valores padrão.