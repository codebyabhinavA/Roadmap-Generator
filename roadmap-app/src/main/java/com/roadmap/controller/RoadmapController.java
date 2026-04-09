package com.roadmap.controller;

import com.roadmap.model.ApiModels.*;
import com.roadmap.service.ClaudeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RoadmapController {

    private final ClaudeService claudeService;

    public RoadmapController(ClaudeService claudeService) {
        this.claudeService = claudeService;
    }

    @PostMapping("/questions")
    public ResponseEntity<?> getQuestions(@RequestBody GoalRequest request) {
        try {
            if (request.goal() == null || request.goal().isBlank()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Goal cannot be empty."));
            }
            var questions = claudeService.generateQuestions(request.goal());
            return ResponseEntity.ok(new QuestionsResponse(questions));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("Failed to generate questions: " + e.getMessage()));
        }
    }

    @PostMapping("/roadmap")
    public ResponseEntity<?> getRoadmap(@RequestBody RoadmapRequest request) {
        try {
            if (request.goal() == null || request.goal().isBlank()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Goal cannot be empty."));
            }
            if (request.answers() == null || request.answers().isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Answers cannot be empty."));
            }
            var roadmap = claudeService.generateRoadmap(request.goal(), request.answers());
            return ResponseEntity.ok(roadmap);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("Failed to generate roadmap: " + e.getMessage()));
        }
    }
}
