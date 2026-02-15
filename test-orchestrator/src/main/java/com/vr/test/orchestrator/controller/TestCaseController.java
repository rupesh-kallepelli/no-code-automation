package com.vr.test.orchestrator.controller;

import com.vr.test.orchestrator.service.KafkaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class TestCaseController {
    private final KafkaService kafkaService;

    public TestCaseController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @PostMapping("publish")
    public String publishToKafka(@RequestParam String topic, @RequestBody String testCase) {
        kafkaService.sendMessage(topic, testCase);
        return "Published to kafka!!";
    }

}
