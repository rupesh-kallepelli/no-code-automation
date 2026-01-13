package com.vr.ai.automation.planner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AIPlanner {

    private final String authToken;
    private final String prompt;
    private static final String OLLAMA_URL = "https://openrouter.ai/api/v1/chat/completions";

    private final ObjectMapper mapper = new ObjectMapper();

    private static final Pattern JSON_PATTERN = Pattern.compile("\\{[\\s\\S]*\\}");

    public AIPlanner(
            @Value("${ai.authToken}") String authToken,
            @Value("classpath:prompt.txt") Resource promptResource
    ) throws IOException {
        this.authToken = authToken;
        this.prompt = new String(promptResource.getInputStream().readAllBytes());
    }

    public String generatePlan(String testCase) {

        try {
            // ✅ Build request JSON safely
            ObjectNode root = mapper.createObjectNode();
            root.put("model", "meta-llama/llama-3.1-8b-instruct");
            root.put("stream", false);

            ArrayNode messages = mapper.createArrayNode();

            ObjectNode systemMsg = mapper.createObjectNode();
            systemMsg.put("role", "system");
            systemMsg.put("content", prompt);

            ObjectNode userMsg = mapper.createObjectNode();
            userMsg.put("role", "user");
            userMsg.put("content", testCase); // ✅ safely escaped

            messages.add(systemMsg);
            messages.add(userMsg);

            root.set("messages", messages);

            String requestBody = mapper.writeValueAsString(root);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OLLAMA_URL))
                    .header("Authorization", "Bearer " + authToken)
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofMinutes(10))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.body() == null || response.body().isBlank()) {
                throw new RuntimeException("Ollama returned empty HTTP body");
            }
            System.out.println("RAW OLLAMA RESPONSE:");
            String body = response.body().trim();
            System.out.println(body);
            JsonNode responseJson = mapper.readTree(body);
            String content = responseJson
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            if (content == null || content.isBlank()) {
                throw new RuntimeException("Ollama returned empty message.content");
            }

            Matcher matcher = JSON_PATTERN.matcher(content);
            if (!matcher.find()) {
                throw new RuntimeException("No JSON found in LLM output:\n" + content);
            }

            return matcher.group();

        } catch (Exception e) {
            throw new RuntimeException("AI planning failed", e);
        }
    }
}
