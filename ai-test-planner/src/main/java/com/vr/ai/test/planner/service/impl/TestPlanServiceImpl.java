package com.vr.ai.test.planner.service.impl;

import com.vr.ai.test.planner.service.TestPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestPlanServiceImpl implements TestPlanService {

    private final ChatClient chatClient;
    private final String testCaseSystemPrompt;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(groupId = "test-planner", topics = "COMPUTE_NL_TEST_CASE")
    @Override
    public void consumeAndProcessTest(String message) {
        String response = chatClient.prompt()
                .system(testCaseSystemPrompt)
                .user(message)
                .call()
                .content();
        kafkaTemplate.send("JSON_TEST_CASE", response);
    }
}
