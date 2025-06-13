package ru.practicum;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.practicum.util.SchemaRegistryUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(SchemaRegistryClient schemaRegistryClient, @Value("${kafka.topic.name}") String topic) {
        return args -> {
            SchemaRegistryUtil.registerSchema(schemaRegistryClient, topic);
        };
    }
}