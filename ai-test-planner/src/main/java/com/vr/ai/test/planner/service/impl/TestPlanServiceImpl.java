package com.vr.ai.test.planner.service.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.ai.test.planner.exception.JsonParsingException;
import com.vr.ai.test.planner.model.testcase.TestCase;
import com.vr.ai.test.planner.service.TestPlanService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestPlanServiceImpl implements TestPlanService {

    private final ChatClient chatClient;
    private final String testCaseSystemPrompt;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(groupId = "test-planner", topics = "COMPUTE_NL_TEST_CASE")
    @Override
    public void consumeAndProcessTest(String message) {
        TestCase testCase = getTestCase(message);
        try {
            kafkaTemplate.send("JSON_TEST_CASE", objectMapper.writeValueAsString(testCase));
        } catch (JsonProcessingException e) {
            throw new JsonParsingException("Unable to parse the testcase to json");
        }
    }

    @Override
    public TestCase getTestCase(String message) {
        return chatClient.prompt()
                .system(testCaseSystemPrompt)
                .user(message)
                .call()
                .entity(TestCase.class);
    }
}
