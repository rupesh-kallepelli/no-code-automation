package com.vr.ai.test.planner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AIPlannerController {

    private final ChatClient chatClient;
    private final String testCaseSystemPrompt;

    @PostMapping("/generate-test")
    public ResponseEntity<String> generateTest(@RequestBody String userInput) {

        String response = chatClient.prompt()
                .system(testCaseSystemPrompt)
                .user(userInput)
                .call()
                .content();

        return ResponseEntity.ok(response);
    }
}
