# Kafka Producer Demo

This project demonstrates how to create a Kafka producer in Java and send messages to a Kafka topic.

## Prerequisites

- Java 11 or higher
- Apache Kafka
- Maven

## Setup

1. **Install Apache Kafka**: Follow the [Kafka Quickstart](https://kafka.apache.org/quickstart) guide to install and run Kafka on your local machine.

2. **Create a Kafka Topic**: Open a terminal and create a topic named `demo_java` with the following command:
    ```bash
    kafka-topics --create --topic demo_java --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
    ```

## Running the Producer

1. **Clone the repository**:
    ```bash
    git clone <repository-url>
    cd <repository-directory>
    ```

2. **Build the project**:
    ```bash
    mvn clean install
    ```

3. **Run the producer**:
    ```bash
    mvn exec:java -Dexec.mainClass="code.with.vanilson.produtor.ProducerDemo"
    ```

## Verifying Message Delivery

To verify that the messages were sent successfully, you can use the Kafka console consumer:

```bash
kafka-console-consumer --bootstrap-server localhost:9092 --topic demo_java --from-beginning
```

### Configuration
The producer properties are configured in the ProducerDemo class. Here are the key configurations:  
- **bootstrap.servers**: The address of the Kafka broker.
- **key.serializer**: The serializer class for the key.
- **value.serializer**: The serializer class for the value.

### Additional Kafka Commands
1. Reset Offsets
```bash
kafka-consumer-groups --reset-offsets --group my-group --topic demo_java --to-earliest --execute --bootstrap-server localhost:9092
```
2. Delete Consumer Group
```bash
kafka-consumer-groups --delete --group my-group --bootstrap-server localhost:9092
```

3. Preferred Replica Election
 ```bash
kafka-preferred-replica-election --bootstrap-server localhost:9092
```  
4. Set Minimum In-Sync Replica
 ```bash
kafka-configs --alter --entity-type topics --entity-name demo_java --add-config min.insync.replicas=2 --bootstrap-server localhost:9092
```

### Producer Acknowledgements
**Acks All**:
 ```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java --producer-property acks=all
 ```
**Acks 1**:
 ```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java --producer-property acks=1
 ```
**Acks 0**:
 ```bash
kafka-console-producer --broker-list localhost:9092 --topic demo_java --producer-property acks=0
 ```