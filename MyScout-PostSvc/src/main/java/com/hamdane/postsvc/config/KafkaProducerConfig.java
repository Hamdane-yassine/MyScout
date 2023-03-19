package com.hamdane.postsvc.config;

import com.hamdane.postsvc.dto.PostEventPayload;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;


    public Map<String, Object> producerConfig() {
        HashMap<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return configProps;
    }

    @Bean
    public ProducerFactory<String, PostEventPayload> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig(), new StringSerializer(), new JsonSerializer<PostEventPayload>().noTypeInfo());
    }

    @Bean
    public KafkaTemplate<String, PostEventPayload> kafkaTemplate(ProducerFactory<String, PostEventPayload> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
