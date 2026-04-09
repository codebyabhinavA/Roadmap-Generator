package com.sdg.roadmap.service;

import com.sdg.roadmap.model.RoadmapRequest;
import com.sdg.roadmap.model.RoadmapResponse;
import com.sdg.roadmap.model.RoadmapResponse.RoadmapStep;
import com.sdg.roadmap.model.RoadmapResponse.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Core service that generates a personalised SDG roadmap
 * based on the user's goal, selected SDG, and question answers.
 */
@Service
public class RoadmapService {

    // ---- SDG Name Lookup ----
    private static final Map<String, String> SDG_NAMES = new LinkedHashMap<>();
    static {
        SDG_NAMES.put("1",  "No Poverty");
        SDG_NAMES.put("2",  "Zero Hunger");
        SDG_NAMES.put("3",  "Good Health & Well-being");
        SDG_NAMES.put("4",  "Quality Education");
        SDG_NAMES.put("5",  "Gender Equality");
        SDG_NAMES.put("6",  "Clean Water & Sanitation");
        SDG_NAMES.put("7",  "Affordable & Clean Energy");
        SDG_NAMES.put("8",  "Decent Work & Economic Growth");
        SDG_NAMES.put("9",  "Industry, Innovation & Infrastructure");
        SDG_NAMES.put("10", "Reduced Inequalities");
        SDG_NAMES.put("11", "Sustainable Cities & Communities");
        SDG_NAMES.put("12", "Responsible Consumption & Production");
        SDG_NAMES.put("13", "Climate Action");
        SDG_NAMES.put("14", "Life Below Water");
        SDG_NAMES.put("15", "Life on Land");
        SDG_NAMES.put("16", "Peace, Justice & Strong Institutions");
        SDG_NAMES.put("17", "Partnerships for the Goals");
    }

    // ---- Entry Point ----
    public RoadmapResponse generate(RoadmapRequest request) {
        String sdgId   = request.getSdgId();
        String goal    = request.getGoal();
        Map<String, String> answers = request.getAnswers();

        String sdgName = SDG_NAMES.getOrDefault(sdgId, "Sustainable Development");
        String timeline = answers.getOrDefault("timeline", "6–12 months");

        RoadmapResponse response = new RoadmapResponse();
        response.setSdgId(sdgId);
        response.setSdgName(sdgName);
        response.setGoalSummary(truncate(goal, 80));
        response.setDescription(buildDescription(sdgId, sdgName, timeline, answers));
        response.setSteps(buildSteps(sdgId, goal, timeline, answers));
        response.setResources(buildResources(sdgId));
        return response;
    }

    // ---- Description Builder ----
    private String buildDescription(String sdgId, String sdgName, String timeline,
                                    Map<String, String> answers) {
        String experience = answers.getOrDefault("experience", "");
        String location   = answers.getOrDefault("location", "your area");
        if (location.isBlank()) location = "your area";

        return String.format(
            "A personalised roadmap aligned to SDG %s: %s. " +
            "Tailored for %s implementation over %s.%s",
            sdgId, sdgName, location, timeline,
            experience.isBlank() ? "" : " Experience level: " + experience + "."
        );
    }

    // ---- Step Builder (SDG-specific) ----
    private List<RoadmapStep> buildSteps(String sdgId, String goal,
                                          String timeline, Map<String, String> answers) {
        return switch (sdgId) {
            case "4"  -> educationSteps(goal, timeline, answers);
            case "3"  -> healthSteps(goal, timeline, answers);
            case "13" -> climateSteps(goal, timeline, answers);
            case "1"  -> povertySteps(goal, timeline, answers);
            default   -> genericSteps(goal, timeline, answers);
        };
    }

    // ---- SDG 4 — Education ----
    private List<RoadmapStep> educationSteps(String goal, String timeline,
                                              Map<String, String> answers) {
        String hours = answers.getOrDefault("hours_per_week", "5–10 hrs");
        return List.of(
            new RoadmapStep("Assess & Set Learning Objectives",
                "Audit your current knowledge and define specific skills or qualifications you need. " +
                "Use tools like a skills gap analysis or career mapping.", "Week 1–2"),
            new RoadmapStep("Identify Learning Pathways",
                "Research courses, certifications, mentors, and institutions. " +
                "Select 2–3 primary learning channels suited to your availability (" + hours + "/week).", "Week 2–3"),
            new RoadmapStep("Build a Study Schedule",
                "Create a weekly study timetable. Block dedicated time slots and use spaced-repetition " +
                "techniques to retain information effectively.", "Week 3–4"),
            new RoadmapStep("Engage with a Learning Community",
                "Join study groups, online forums, or local clubs. Teaching others is one of the fastest " +
                "ways to consolidate your own learning.", "Month 2"),
            new RoadmapStep("Apply Through Projects",
                "Work on real projects that demonstrate your new skills — a portfolio, volunteer work, " +
                "or a personal initiative relevant to your goal.", "Month 3–4"),
            new RoadmapStep("Evaluate & Certify",
                "Take assessments, earn certifications, or gather feedback from mentors. " +
                "Document your progress and adjust your plan for the remaining timeline.", "By " + timeline)
        );
    }

    // ---- SDG 3 — Health ----
    private List<RoadmapStep> healthSteps(String goal, String timeline,
                                           Map<String, String> answers) {
        String focus = answers.getOrDefault("health_focus", "health & well-being");
        return List.of(
            new RoadmapStep("Conduct a Health Needs Assessment",
                "Identify the specific health challenges in your target area or community. " +
                "Gather data through surveys, interviews, or existing health reports.", "Week 1–2"),
            new RoadmapStep("Define Clear Health Outcomes",
                "Set measurable health targets aligned with your focus area: " + focus + ". " +
                "Use WHO frameworks or national health guidelines as reference.", "Week 2–3"),
            new RoadmapStep("Map Existing Resources & Gaps",
                "Identify what healthcare services, organizations, or tools already exist. " +
                "Document gaps your initiative can address.", "Week 3–5"),
            new RoadmapStep("Design & Launch an Intervention",
                "Create your health program, campaign, or service. Start with a small pilot, " +
                "gather participant feedback, and refine your approach.", "Month 2–3"),
            new RoadmapStep("Build Partnerships",
                "Collaborate with local health authorities, NGOs, or community leaders. " +
                "Partnerships extend your reach and add credibility.", "Month 3–4"),
            new RoadmapStep("Monitor, Evaluate & Scale",
                "Track health metrics, document outcomes, and use the evidence to scale " +
                "your initiative and attract further support.", "By " + timeline)
        );
    }

    // ---- SDG 13 — Climate Action ----
    private List<RoadmapStep> climateSteps(String goal, String timeline,
                                            Map<String, String> answers) {
        String scale = answers.getOrDefault("scale", "local");
        return List.of(
            new RoadmapStep("Understand the Local Climate Context",
                "Research climate risks specific to your region (" + scale + " scale). " +
                "Review IPCC reports, local government plans, and existing initiatives.", "Week 1–2"),
            new RoadmapStep("Calculate Your Baseline Impact",
                "Measure the current carbon footprint, emissions, or ecological impact " +
                "you intend to reduce. Establish a clear baseline for comparison.", "Week 2–3"),
            new RoadmapStep("Design Your Climate Solution",
                "Develop a concrete intervention plan — whether it's renewable energy, " +
                "reforestation, policy advocacy, or community education.", "Week 3–5"),
            new RoadmapStep("Pilot & Test",
                "Run a small-scale version of your climate action. Document results, " +
                "challenges, and unintended consequences carefully.", "Month 2–3"),
            new RoadmapStep("Mobilize Stakeholders",
                "Engage local governments, businesses, schools, or media. " +
                "Climate action requires coalition-building to be effective.", "Month 3–4"),
            new RoadmapStep("Report Impact & Scale Up",
                "Quantify emissions reduced, land restored, or awareness raised. " +
                "Use this data to replicate or expand your initiative.", "By " + timeline)
        );
    }

    // ---- SDG 1 — No Poverty ----
    private List<RoadmapStep> povertySteps(String goal, String timeline,
                                            Map<String, String> answers) {
        String target = answers.getOrDefault("target_group", "target communities");
        return List.of(
            new RoadmapStep("Research Poverty Dimensions",
                "Study multidimensional poverty (income, education, healthcare, housing) " +
                "affecting " + target + ". Use World Bank or UNDP data as starting points.", "Week 1–2"),
            new RoadmapStep("Identify Root Causes",
                "Conduct community consultations or focus groups. Understanding root causes " +
                "is essential before designing effective solutions.", "Week 2–4"),
            new RoadmapStep("Design an Economic Intervention",
                "Develop an income-generation scheme, financial literacy program, " +
                "or livelihood support tailored to your findings.", "Month 1–2"),
            new RoadmapStep("Pilot with a Small Group",
                "Test your approach with a small cohort. Gather quantitative and qualitative " +
                "outcomes — income change, savings, quality of life improvements.", "Month 2–4"),
            new RoadmapStep("Secure Funding & Partnerships",
                "Apply for microfinance, grants, or CSR funding. Partner with local " +
                "NGOs or government welfare programs for sustainability.", "Month 3–5"),
            new RoadmapStep("Scale & Document Impact",
                "Expand the program, refine your model, and publish your impact data " +
                "to attract further investment and policy attention.", "By " + timeline)
        );
    }

    // ---- Generic Fallback Steps ----
    private List<RoadmapStep> genericSteps(String goal, String timeline,
                                            Map<String, String> answers) {
        String resources = answers.getOrDefault("resources", "limited resources");
        return List.of(
            new RoadmapStep("Define & Research Your Goal",
                "Document your goal clearly: \"" + truncate(goal, 60) + "\". " +
                "Research existing work in this space and identify key actors.", "Week 1–2"),
            new RoadmapStep("Build Your Knowledge Base",
                "Read, take courses, and talk to experts. Identify the skills and " +
                "knowledge gaps you need to fill to move forward.", "Week 2–4"),
            new RoadmapStep("Create a Detailed Action Plan",
                "Break your goal into monthly milestones. Set SMART targets. " +
                "Account for your current resources: " + resources + ".", "Month 1"),
            new RoadmapStep("Launch a Pilot Initiative",
                "Start small and test your core assumptions. A pilot reduces risk and " +
                "gives you real-world feedback before scaling.", "Month 2–3"),
            new RoadmapStep("Measure Progress & Iterate",
                "Track key metrics. Celebrate wins, document failures honestly, and " +
                "adjust your strategy based on evidence and feedback.", "Month 4–5"),
            new RoadmapStep("Scale & Sustain",
                "Expand your reach, diversify your funding or support base, and " +
                "create systems that keep the initiative alive long-term.", "By " + timeline)
        );
    }

    // ---- Resource Builder ----
    private List<Resource> buildResources(String sdgId) {
        List<Resource> base = new ArrayList<>(List.of(
            new Resource("🌐", "UN SDG Official Portal",
                "sdgs.un.org — Targets, indicators, and global progress reports for all 17 SDGs."),
            new Resource("📖", "SDG Academy (edX)",
                "Free and paid courses on sustainable development by leading global universities."),
            new Resource("💡", "Open SDG Data Hub",
                "opensdg.org — Data tools and dashboards to track and report SDG progress.")
        ));

        // SDG-specific resources
        switch (sdgId) {
            case "4" -> base.add(new Resource("🎓", "UNESCO Education Resources",
                "unesco.org/education — Policy tools, research, and frameworks for quality education."));
            case "3" -> base.add(new Resource("🏥", "WHO Health Resources",
                "who.int — Global health guidelines, toolkits, and statistics."));
            case "13" -> base.add(new Resource("🌱", "IPCC Climate Reports",
                "ipcc.ch — Authoritative climate science and adaptation guidance."));
            case "1" -> base.add(new Resource("💰", "World Bank Poverty Data",
                "data.worldbank.org — Global poverty statistics and development research."));
            case "7" -> base.add(new Resource("⚡", "IRENA Renewable Energy",
                "irena.org — Data, tools, and reports on clean energy transition."));
            default  -> base.add(new Resource("🤝", "SDG Partnership Platform",
                "sdgs.un.org/partnerships — Find and register SDG-aligned partnerships globally."));
        }

        return base;
    }

    // ---- Utility ----
    private String truncate(String text, int maxLen) {
        if (text == null) return "";
        return text.length() > maxLen ? text.substring(0, maxLen) + "..." : text;
    }
}
