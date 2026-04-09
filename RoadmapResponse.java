package com.sdg.roadmap.model;

import java.util.List;

/**
 * Response payload sent back to the frontend.
 * Contains the structured roadmap with steps and resources.
 */
public class RoadmapResponse {

    private String sdgId;
    private String sdgName;
    private String goalSummary;
    private String description;
    private List<RoadmapStep> steps;
    private List<Resource> resources;

    // ---- Inner Classes ----

    public static class RoadmapStep {
        private String title;
        private String description;
        private String timeline;

        public RoadmapStep(String title, String description, String timeline) {
            this.title       = title;
            this.description = description;
            this.timeline    = timeline;
        }

        public String getTitle()       { return title; }
        public String getDescription() { return description; }
        public String getTimeline()    { return timeline; }
    }

    public static class Resource {
        private String icon;
        private String title;
        private String detail;

        public Resource(String icon, String title, String detail) {
            this.icon   = icon;
            this.title  = title;
            this.detail = detail;
        }

        public String getIcon()   { return icon; }
        public String getTitle()  { return title; }
        public String getDetail() { return detail; }
    }

    // ---- Getters & Setters ----

    public String getSdgId()       { return sdgId; }
    public void setSdgId(String v) { this.sdgId = v; }

    public String getSdgName()       { return sdgName; }
    public void setSdgName(String v) { this.sdgName = v; }

    public String getGoalSummary()       { return goalSummary; }
    public void setGoalSummary(String v) { this.goalSummary = v; }

    public String getDescription()       { return description; }
    public void setDescription(String v) { this.description = v; }

    public List<RoadmapStep> getSteps()       { return steps; }
    public void setSteps(List<RoadmapStep> v) { this.steps = v; }

    public List<Resource> getResources()       { return resources; }
    public void setResources(List<Resource> v) { this.resources = v; }
}
