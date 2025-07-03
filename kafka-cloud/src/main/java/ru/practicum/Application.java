package ru.practicum;

import ru.practicum.dto.Topics;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.practicum.util.SchemaRegistryUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(SchemaRegistryClient schemaRegistryClient) {
        return args -> {
            SchemaRegistryUtil.registerSchema(schemaRegistryClient, Topics.ORDERS_TOPIC);
        };
    }
}