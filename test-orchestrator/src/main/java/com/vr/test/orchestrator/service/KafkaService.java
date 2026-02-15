package com.vr.test.orchestrator.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Produce a message to topic
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

    // Consume messages from topic
    @KafkaListener(topics = "rupesh", groupId = "test-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}