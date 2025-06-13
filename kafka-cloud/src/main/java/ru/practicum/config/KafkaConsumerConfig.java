package ru.practicum.config;

import java.util.Map;
import java.util.HashMap;
import ru.practicum.dto.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.apache.kafka.common.serialization.StringDeserializer;
import ru.practicum.serialization.OrderDeserializer;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Value(value = "${kafka.consumer.group.id}")
    private String groupId;
    @Value(value = "${kafka.consumer.auto.offset.reset}")
    private String autoOffset;
    @Value(value = "${kafka.consumer.enable.auto.commit}")
    private String enableAutoCommit;
    @Value(value = "${kafka.security.protocol}")
    private String securityProtocol;
    @Value(value = "${kafka.sasl.mechanism}")
    private String saslMechanism;
    @Value(value = "${kafka.sasl.jaas.config}")
    private String saslJaasConfig;
    @Value(value = "${kafka.ssl.truststore.location}")
    private String truststoreLocation;
    @Value(value = "${kafka.ssl.truststore.password}")
    private String truststorePassword;
    @Value(value = "${kafka.ssl.user}")
    private String kafkaUser;
    @Value(value = "${kafka.ssl.password}")
    private String kafkaPassword;

    @Bean
    public ConsumerFactory<String, Order> orderConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, OrderDeserializer.class.getName());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffset);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        config.put("security.protocol", securityProtocol);
        config.put("sasl.mechanism", saslMechanism);
        config.put("sasl.jaas.config", String.format(saslJaasConfig, kafkaUser, kafkaPassword));
        config.put("ssl.truststore.location", truststoreLocation);
        config.put("ssl.truststore.password", truststorePassword);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Order> orderKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Order> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory());
        return factory;
    }
}