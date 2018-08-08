package com.example.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.schema.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by HurleyD on 08/08/2018.
 */
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.consumer.properties.schema.registry.url}")
    private String endpoint;

    @Bean
    public SchemaRegistryClient confluentSchemaRegistryClient() {
        ConfluentSchemaRegistryClient client = new ConfluentSchemaRegistryClient();
        client.setEndpoint(endpoint);
        return client;
    }

}
