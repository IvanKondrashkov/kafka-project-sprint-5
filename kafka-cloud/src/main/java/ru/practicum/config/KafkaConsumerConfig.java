package ru.practicum.config;

import java.util.Map;
import ru.practicum.dto.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import ru.practicum.serialization.OrderDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    private final SslBundles sslBundles;
    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, Order> orderConsumerFactory() {
        Map<String, Object> config = kafkaProperties.buildConsumerProperties(sslBundles);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, OrderDeserializer.class.getName());
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Order> orderKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Order> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory());
        return factory;
    }
}