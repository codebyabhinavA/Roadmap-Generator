package com.roadmap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadmap.model.ApiModels.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class ClaudeService {

    @Value("${anthropic.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.anthropic.com/v1/messages";
    private static final String MODEL = "claude-sonnet-4-20250514";
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public List<String> generateQuestions(String goal) throws Exception {
        String prompt = """
            The user's goal is: "%s"
            
            Generate exactly 5 short, insightful follow-up questions to understand their situation better.
            Focus on: current skill/experience level, available time per week, timeline/deadline,
            resources or constraints, and what success looks like to them.
            
            Return ONLY a valid JSON array of 5 question strings. No explanation, no markdown, no extra text.
            Example format: ["Question 1?", "Question 2?", "Question 3?", "Question 4?", "Question 5?"]
            """.formatted(goal);

        String response = callClaude(prompt);
        String cleaned = response.trim().replaceAll("```json", "").replaceAll("```", "").trim();
        JsonNode arr = mapper.readTree(cleaned);
        return mapper.convertValue(arr, mapper.getTypeFactory().constructCollectionType(List.class, String.class));
    }

    public RoadmapResponse generateRoadmap(String goal, List<Map<String, String>> answers) throws Exception {
        StringBuilder answerBlock = new StringBuilder();
        for (Map<String, String> qa : answers) {
            answerBlock.append("Q: ").append(qa.get("question")).append("\n");
            answerBlock.append("A: ").append(qa.get("answer")).append("\n\n");
        }

        String prompt = """
            Goal: "%s"
            
            User's answers to follow-up questions:
            %s
            
            Create a detailed, realistic, and motivating roadmap tailored to this person.
            Return ONLY a valid JSON object with this exact structure (no markdown, no extra text):
            {
              "title": "Short inspiring roadmap title",
              "summary": "Two-sentence summary of the journey and what they will achieve.",
              "phases": [
                {
                  "number": 1,
                  "title": "Phase title",
                  "duration": "e.g. 2 weeks",
                  "objective": "One clear objective sentence.",
                  "actionSteps": ["Step 1", "Step 2", "Step 3", "Step 4"]
                }
              ]
            }
            
            Include 4 to 6 phases. Make steps specific and actionable, not vague.
            """.formatted(goal, answerBlock.toString());

        String response = callClaude(prompt);
        String cleaned = response.trim().replaceAll("```json", "").replaceAll("```", "").trim();
        return mapper.readValue(cleaned, RoadmapResponse.class);
    }

    private String callClaude(String userPrompt) throws Exception {
        AnthropicRequest requestBody = new AnthropicRequest(
            MODEL,
            2000,
            List.of(new AnthropicMessage("user", userPrompt))
        );

        String jsonBody = mapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("Content-Type", "application/json")
            .header("x-api-key", apiKey)
            .header("anthropic-version", "2023-06-01")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Claude API error: " + response.statusCode() + " — " + response.body());
        }

        JsonNode root = mapper.readTree(response.body());
        return root.get("content").get(0).get("text").asText();
    }
}
