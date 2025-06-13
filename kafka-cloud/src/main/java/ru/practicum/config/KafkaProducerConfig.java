package ru.practicum.config;

import java.util.Map;
import java.util.HashMap;
import ru.practicum.dto.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import io.confluent.kafka.serializers.KafkaJsonSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.apache.kafka.common.serialization.StringSerializer;

@Configuration
public class KafkaProducerConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Value(value = "${kafka.producer.acks}")
    private String acks;
    @Value(value = "${kafka.producer.retries}")
    private Integer retries;
    @Value(value = "${kafka.producer.properties[min.insync.replicas]}")
    private Integer minInSyncReplicas;
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
    public ProducerFactory<String, Order> orderProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.ACKS_CONFIG, acks);
        config.put(ProducerConfig.RETRIES_CONFIG, retries);
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class.getName());
        config.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, minInSyncReplicas);
        config.put("security.protocol", securityProtocol);
        config.put("sasl.mechanism", saslMechanism);
        config.put("sasl.jaas.config", String.format(saslJaasConfig, kafkaUser, kafkaPassword));
        config.put("ssl.truststore.location", truststoreLocation);
        config.put("ssl.truststore.password", truststorePassword);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Order> orderKafkaTemplate() {
        return new KafkaTemplate<>(orderProducerFactory());
    }
}