package com.example.kafka;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class SchemaRegistryStubbedTestConfiguration {

    @Primary
    @Bean
    public SchemaRegistryClient schemaRegistryClient() {
        SchemaRegistryClient client = new StubSchemaRegistryClient();
        return client;
    }
}
