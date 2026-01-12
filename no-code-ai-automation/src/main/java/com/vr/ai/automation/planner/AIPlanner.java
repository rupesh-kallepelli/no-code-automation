package com.vr.ai.automation.planner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AIPlanner {

    private static final String OLLAMA_URL =
            "https://openrouter.ai/api/v1/chat/completions";
//            "https://ollama-kallepallirupesh-dev.apps.rm2.thpm.p1.openshiftapps.com/api/chat";
//    private static final String OPENAI_URL =
//            "https://api.openai.com/v1/chat/completions";
    private final ObjectMapper mapper = new ObjectMapper();

    private static final Pattern JSON_PATTERN =
            Pattern.compile("\\{[\\s\\S]*\\}");

    public String generatePlan(String testCase) {

        try {
            // ✅ Build request JSON safely
            ObjectNode root = mapper.createObjectNode();
            root.put("model", "meta-llama/llama-3.1-8b-instruct");
            root.put("stream", false);

            ArrayNode messages = mapper.createArrayNode();

            ObjectNode systemMsg = mapper.createObjectNode();
            systemMsg.put("role", "system");
            systemMsg.put("content",
                    """
                            You are a test automation planner.
                            
                            You MUST return a SINGLE valid JSON object with EXACTLY the structure defined below.
                            
                            REQUIRED JSON STRUCTURE:
                            {
                              "testName": "<string>",
                              "steps": [
                                {
                                  "action": "<ONE of: NAVIGATE | TYPE | CLICK | WAIT_FOR_VISIBLE>",
                                  "selector": "<string | null>",
                                  "value": "<string | null>",
                                  "timeoutMs": <number | null>
                                }
                              ]
                            }
                            
                            CRITICAL RULES:
                            1. Return ONLY raw JSON
                            2. Do NOT use markdown
                            3. Do NOT add explanations
                            4. Each step MUST have exactly ONE action
                            5. NEVER combine actions
                            6. If a URL is present, the FIRST step MUST be NAVIGATE
                            7. NAVIGATE must have value=url and selector=null
                            8. WAIT_FOR_VISIBLE must have a CSS selector and value=null
                            9. NEVER use URL paths as selectors
                            10. timeoutMs MUST be a number
                            11. SELECTOR VALUE SHOULD BE A STRICT CSS SELECTOR NOT JSON, NOT ANY OTHER VALUE ONLY CSS SELECTOR EX: input[name="username"], #someId, .someClass
                            """
            );

            ObjectNode userMsg = mapper.createObjectNode();
            userMsg.put("role", "user");
            userMsg.put("content", testCase); // ✅ safely escaped

            messages.add(systemMsg);
            messages.add(userMsg);

            root.set("messages", messages);

            String requestBody = mapper.writeValueAsString(root);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OLLAMA_URL))
                    .header("Authorization", "Bearer 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")
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofMinutes(10))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("RAW OLLAMA RESPONSE:");
            System.out.println(response.body());

//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(OPENAI_URL))
//                    .timeout(Duration.ofSeconds(30))
//                    .header("Authorization", "Bearer " + System.getenv("OPENAI_API_KEY"))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                    .build();
//
//            HttpResponse<String> response =
//                    HttpClient.newHttpClient()
//                            .send(request, HttpResponse.BodyHandlers.ofString());
//
////            JsonNode root = mapper.readTree(response.body());

            if (response.body() == null || response.body().isBlank()) {
                throw new RuntimeException("Ollama returned empty HTTP body");
            }

            JsonNode responseJson = mapper.readTree(response.body());
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
