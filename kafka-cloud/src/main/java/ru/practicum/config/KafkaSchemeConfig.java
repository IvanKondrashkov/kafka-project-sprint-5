package ru.practicum.config;

import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;

@Configuration
public class KafkaSchemeConfig {
    @Value(value = "${kafka.schema.registry.url}")
    private String schemaRegistryUrl;
    @Value(value = "${kafka.ssl.truststore.location}")
    private String truststoreLocation;
    @Value(value = "${kafka.ssl.truststore.password}")
    private String truststorePassword;
    @Value(value = "${kafka.ssl.user}")
    private String kafkaUser;
    @Value(value = "${kafka.ssl.password}")
    private String kafkaPassword;

    @Bean
    public SchemaRegistryClient schemaRegistryClient() {
        return new CachedSchemaRegistryClient(
                schemaRegistryUrl,
                10,
                Map.of(
                        "basic.auth.credentials.source", "USER_INFO",
                        "basic.auth.user.info", String.format("%s:%s", kafkaUser, kafkaPassword),
                        "schema.registry.ssl.truststore.location", truststoreLocation,
                        "schema.registry.ssl.truststore.password", truststorePassword
                )
        );
    }
}