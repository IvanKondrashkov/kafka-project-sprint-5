package ru.practicum.service;

import java.util.UUID;
import ru.practicum.dto.Order;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProcessorImpl implements MessageProcessor {
    @Value(value = "${kafka.topic.name}")
    private String topic;
    private final KafkaTemplate<String, Order> kafkaTemplate;

    @Override
    public void sendMessage(Order order) {
        log.info("send order={}", order);
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), order);
    }

    @KafkaListener(topics = "orders", groupId = "orders", containerFactory = "orderKafkaListenerContainerFactory")
    public void listenMessage(Order order) {
        log.info("receive order={}", order);
    }
}