package code.with.vanilson.webflux.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

/**
 * KafkaTopicConfig
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-24
 */
@Configurable
public class KafkaTopicConfig {

    @Value("${application.config.topics}")
    private String topic;

    // Methods create a new topics.
    @Bean
    public NewTopic topic1() {
        return TopicBuilder
                .name(topic)
                .build();
    }

}