package code.with.vanilson;

import code.with.vanilson.produtor.ProducerDemoKeys;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikimediaProducer {

    private static final Logger log = LoggerFactory.getLogger(WikimediaProducer.class.getName());

    private WikimediaProducer() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Método para iniciar a produção de eventos do Wikimedia RecentChange API para um tópico Kafka.
     */
    public static void startProdutorWikimedia() {
        // Criar propriedades do produtor Kafka
        Properties properties = ProducerDemoKeys.getProperties();

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            String topic = "wikimedia-recentchange";
            // Criar um manipulador de eventos para o EventSource
            EventHandler handler = new WikimediaChangeHandler(producer, topic);
            // URL do servidor de ‘streaming’ Wikimedia RecentChange API
            String url = "https://stream.wikimedia.org/v2/stream/recentchange";
            // Criar um EventSource.Builder com o manipulador de eventos e a URL
            EventSource.Builder builder = new EventSource.Builder(handler, URI.create(url));
            // Criar um EventSource com o EventSource.Builder
            EventSource eventSource = builder.build();

            // Iniciar o EventSource para se conectar ao servidor de streaming Wikimedia RecentChange API
            log.info("Iniciando o EventSource");
            eventSource.start();

            // Manter a conexão aberta por 10 minutos
            TimeUnit.MINUTES.sleep(10);
            // Fechar o EventSource para desconectar do servidor de streaming Wikimedia RecentChange API
            eventSource.close();

        } catch (InterruptedException e) {
            log.error("Erro ao dormir a thread", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Erro ao criar o produtor Kafka", e);
        }
    }
}
