package com.vr.test.orchestrator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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
    @KafkaListener(topics = "JSON_TEST_CASE", groupId = "test-orch")
    public void listen(String message) {
        log.info("\n{}", message);
    }
}