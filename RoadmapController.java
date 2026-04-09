package com.sdg.roadmap.controller;

import com.sdg.roadmap.model.RoadmapRequest;
import com.sdg.roadmap.model.RoadmapResponse;
import com.sdg.roadmap.service.RoadmapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller exposing the /api/roadmap endpoint.
 * Accepts POST requests from the frontend and returns a generated roadmap.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")   // Allow requests from frontend dev server
public class RoadmapController {

    private final RoadmapService roadmapService;

    public RoadmapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    /**
     * POST /api/roadmap
     * Body: { goal, sdgId, answers: { ... } }
     * Returns: RoadmapResponse JSON
     */
    @PostMapping("/roadmap")
    public ResponseEntity<RoadmapResponse> generateRoadmap(@RequestBody RoadmapRequest request) {
        if (request.getGoal() == null || request.getGoal().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (request.getSdgId() == null || request.getSdgId().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        RoadmapResponse response = roadmapService.generate(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/health
     * Simple health-check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("SDG Roadmap API is running ✅");
    }
}
