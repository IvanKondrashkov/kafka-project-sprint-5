package ru.practicum.service;

import java.util.UUID;
import ru.practicum.dto.Order;
import ru.practicum.dto.Topics;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProcessorImpl implements MessageProcessor {
    private final KafkaTemplate<String, Order> kafkaTemplate;

    @Override
    public void sendMessage(Order order) {
        log.info("send order={}", order);
        kafkaTemplate.send(Topics.ORDERS_TOPIC, UUID.randomUUID().toString(), order);
    }

    @KafkaListener(topics = Topics.ORDERS_TOPIC, groupId = "orders-id", containerFactory = "orderKafkaListenerContainerFactory")
    public void listenMessage(Order order) {
        log.info("receive order={}", order);
    }
}