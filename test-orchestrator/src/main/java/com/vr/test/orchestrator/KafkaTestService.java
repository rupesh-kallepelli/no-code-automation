package com.vr.test.orchestrator;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaTestService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaTestService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Produce a message to topic
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Sent message: " + message + " to topic: " + topic);
    }

    // Consume messages from topic
    @KafkaListener(topics = "rupesh", groupId = "test-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
