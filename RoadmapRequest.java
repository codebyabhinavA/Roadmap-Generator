package com.sdg.roadmap.model;

import java.util.Map;

/**
 * Incoming request payload from the frontend.
 * Contains the user's goal text, SDG ID, and their answers to the dynamic questions.
 */
public class RoadmapRequest {

    private String goal;
    private String sdgId;
    private Map<String, String> answers;

    // ---- Getters & Setters ----

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public String getSdgId() { return sdgId; }
    public void setSdgId(String sdgId) { this.sdgId = sdgId; }

    public Map<String, String> getAnswers() { return answers; }
    public void setAnswers(Map<String, String> answers) { this.answers = answers; }
}
