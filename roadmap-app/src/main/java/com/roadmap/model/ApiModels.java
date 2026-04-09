package com.roadmap.model;

import java.util.List;
import java.util.Map;

public class ApiModels {

    public record GoalRequest(String goal) {}

    public record QuestionsResponse(List<String> questions) {}

    public record RoadmapRequest(String goal, List<Map<String, String>> answers) {}

    public record Phase(
        int number,
        String title,
        String duration,
        String objective,
        List<String> actionSteps
    ) {}

    public record RoadmapResponse(
        String title,
        String summary,
        List<Phase> phases
    ) {}

    public record ErrorResponse(String error) {}

    // Anthropic API request/response shapes
    public record AnthropicMessage(String role, String content) {}

    public record AnthropicRequest(
        String model,
        int max_tokens,
        List<AnthropicMessage> messages
    ) {}

    public record AnthropicContentBlock(String type, String text) {}

    public record AnthropicResponse(List<AnthropicContentBlock> content) {}
}
