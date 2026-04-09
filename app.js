// ==============================
// SDG ROADMAP — Frontend Logic
// ==============================

const SDG_QUESTIONS = {
  default: [
    { id: "timeline", label: "What is your target timeline?", type: "radio", options: ["1–3 months", "3–6 months", "6–12 months", "1–2 years", "2+ years"] },
    { id: "resources", label: "What resources do you currently have?", type: "radio", options: ["No resources yet", "Some knowledge/skills", "Some funding", "A team/network", "All of the above"] },
    { id: "experience", label: "Your experience level in this area?", type: "radio", options: ["Complete beginner", "Some exposure", "Intermediate", "Expert"] },
    { id: "location", label: "Where will this goal be implemented?", type: "text", placeholder: "e.g. My city, online, globally..." },
    { id: "challenges", label: "What's your biggest challenge right now?", type: "text", placeholder: "e.g. lack of funding, no network..." }
  ],
  "3": [
    { id: "health_focus", label: "Which health area?", type: "radio", options: ["Physical fitness", "Mental health", "Community health", "Healthcare access", "Nutrition"] },
    { id: "timeline", label: "Target timeline?", type: "radio", options: ["1–3 months", "3–6 months", "6–12 months", "1+ year"] },
    { id: "target_group", label: "Who does this goal serve?", type: "text", placeholder: "e.g. yourself, local community, rural areas..." },
    { id: "barriers", label: "Main barriers you face?", type: "text", placeholder: "e.g. cost, awareness, access..." }
  ],
  "4": [
    { id: "edu_level", label: "Current education / skill level?", type: "radio", options: ["Primary/Secondary", "Undergraduate", "Graduate", "Working professional", "Self-taught"] },
    { id: "timeline", label: "Target timeline?", type: "radio", options: ["< 3 months", "3–6 months", "6–12 months", "1–2 years"] },
    { id: "hours_per_week", label: "Hours per week available?", type: "radio", options: ["< 5 hrs", "5–10 hrs", "10–20 hrs", "20+ hrs"] },
    { id: "resources", label: "Access to learning resources?", type: "radio", options: ["Internet only", "Internet + library", "Formal institution", "Limited access"] }
  ],
  "13": [
    { id: "climate_action", label: "What type of climate action?", type: "radio", options: ["Individual lifestyle", "Community project", "Policy/advocacy", "Research", "Business/startup"] },
    { id: "timeline", label: "Target timeline?", type: "radio", options: ["< 3 months", "3–12 months", "1–3 years", "3+ years"] },
    { id: "scale", label: "Scale of impact?", type: "radio", options: ["Personal", "Neighborhood", "City", "National", "Global"] },
    { id: "partners", label: "Do you have partners or collaborators?", type: "radio", options: ["No, working alone", "A few people", "An organization", "Multiple organizations"] }
  ],
  "1": [
    { id: "poverty_focus", label: "Which aspect of poverty?", type: "radio", options: ["Income generation", "Financial literacy", "Community support", "Policy advocacy", "Entrepreneurship"] },
    { id: "timeline", label: "Target timeline?", type: "radio", options: ["< 6 months", "6–12 months", "1–3 years", "3+ years"] },
    { id: "target_group", label: "Who does this serve?", type: "text", placeholder: "e.g. unemployed youth, rural families..." },
    { id: "resources", label: "Resources available?", type: "radio", options: ["None", "Personal savings", "Grants/NGO support", "Community network"] }
  ]
};

let currentGoal = "";
let currentSDG = "";

// ---- SCREEN MANAGER ----
function showScreen(id) {
  document.querySelectorAll('.screen').forEach(s => s.classList.remove('active'));
  const target = document.getElementById(id);
  if (target) target.classList.add('active');
  window.scrollTo(0, 0);
}

// ---- STEP 1 → 2 ----
function proceedToQuestions() {
  const goal = document.getElementById('goalText').value.trim();
  const sdg  = document.getElementById('sdgCategory').value;
  if (!goal) { alert("Please describe your goal before continuing."); return; }
  if (!sdg)  { alert("Please select an SDG category."); return; }
  currentGoal = goal;
  currentSDG  = sdg;
  renderQuestions(sdg);
  showScreen('questions-screen');
}

// ---- RENDER DYNAMIC QUESTIONS ----
function renderQuestions(sdgId) {
  const questions = SDG_QUESTIONS[sdgId] || SDG_QUESTIONS.default;
  const container = document.getElementById('questions-container');
  container.innerHTML = '';
  questions.forEach(q => {
    const div = document.createElement('div');
    div.className = 'input-group';
    div.innerHTML = `<label>${q.label}</label>`;
    if (q.type === 'radio') {
      const rg = document.createElement('div');
      rg.className = 'radio-group';
      q.options.forEach(opt => {
        rg.innerHTML += `
          <label>
            <input type="radio" name="${q.id}" value="${opt}"/>
            ${opt}
          </label>`;
      });
      div.appendChild(rg);
    } else {
      div.innerHTML += `<input type="text" id="q_${q.id}" placeholder="${q.placeholder || ''}"/>`;
    }
    container.appendChild(div);
  });
}

// ---- COLLECT ANSWERS ----
function collectAnswers() {
  const questions = SDG_QUESTIONS[currentSDG] || SDG_QUESTIONS.default;
  const answers = {};
  questions.forEach(q => {
    if (q.type === 'radio') {
      const checked = document.querySelector(`input[name="${q.id}"]:checked`);
      answers[q.id] = checked ? checked.value : "Not specified";
    } else {
      const el = document.getElementById('q_' + q.id);
      answers[q.id] = el ? el.value.trim() : "Not specified";
    }
  });
  return answers;
}

// ---- SUBMIT TO BACKEND ----
async function submitToBackend() {
  const answers = collectAnswers();
  showScreen('loading-screen');

  const payload = {
    goal: currentGoal,
    sdgId: currentSDG,
    answers: answers
  };

  try {
    const response = await fetch('/api/roadmap', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (!response.ok) throw new Error("Server error");
    const data = await response.json();
    renderRoadmap(data);
    showScreen('roadmap-screen');
  } catch (err) {
    console.error(err);
    // Fallback: generate client-side if backend unavailable
    const fallback = generateFallbackRoadmap(currentGoal, currentSDG, answers);
    renderRoadmap(fallback);
    showScreen('roadmap-screen');
  }
}

// ---- RENDER ROADMAP ----
function renderRoadmap(data) {
  // Header
  document.getElementById('roadmap-header').innerHTML = `
    <div class="sdg-badge">SDG ${data.sdgId} — ${data.sdgName}</div>
    <h2>${data.goalSummary}</h2>
    <p>${data.description}</p>
  `;

  // Steps
  const stepsEl = document.getElementById('roadmap-steps');
  stepsEl.innerHTML = `<div class="roadmap-section-title">📍 Your Action Plan</div>`;
  data.steps.forEach((step, i) => {
    const card = document.createElement('div');
    card.className = 'step-card';
    card.style.animationDelay = `${i * 0.08}s`;
    card.innerHTML = `
      <div class="step-number">${i + 1}</div>
      <div>
        <h4>${step.title}</h4>
        <p>${step.description}</p>
        <div class="step-timeline">⏱ ${step.timeline}</div>
      </div>`;
    stepsEl.appendChild(card);
  });

  // Resources
  const resEl = document.getElementById('roadmap-resources');
  resEl.innerHTML = `<div class="roadmap-section-title">📚 Recommended Resources</div>`;
  data.resources.forEach(r => {
    resEl.innerHTML += `
      <div class="resource-card">
        <span class="icon">${r.icon}</span>
        <div><strong>${r.title}</strong><br/><span style="color:var(--muted);font-size:0.85rem">${r.detail}</span></div>
      </div>`;
  });
}

// ---- FALLBACK ROADMAP (client-side) ----
const SDG_NAMES = {
  "1":"No Poverty","2":"Zero Hunger","3":"Good Health & Well-being","4":"Quality Education",
  "5":"Gender Equality","6":"Clean Water","7":"Clean Energy","8":"Decent Work",
  "9":"Innovation","10":"Reduced Inequalities","11":"Sustainable Cities",
  "12":"Responsible Consumption","13":"Climate Action","14":"Life Below Water",
  "15":"Life on Land","16":"Peace & Justice","17":"Partnerships"
};

function generateFallbackRoadmap(goal, sdgId, answers) {
  const timeline = answers.timeline || "6–12 months";
  const name = SDG_NAMES[sdgId] || "Sustainable Development";

  const steps = [
    { title: "Define & Research", description: `Clearly document your goal: "${goal}". Research existing initiatives, key stakeholders, and best practices in this space.`, timeline: "Week 1–2" },
    { title: "Build Your Foundation", description: `Identify the core skills, tools, or partnerships you need. Reach out to at least 3 people or organizations already working in this area.`, timeline: "Week 3–4" },
    { title: "Create an Action Plan", description: `Break your goal into monthly milestones. Set SMART targets (Specific, Measurable, Achievable, Relevant, Time-bound) for each month.`, timeline: "Week 5" },
    { title: "Launch a Pilot", description: `Start small. Run a pilot version of your initiative — test assumptions, gather feedback, and iterate before scaling.`, timeline: "Month 2–3" },
    { title: "Measure & Reflect", description: `Track your progress using simple metrics. Document what's working and what isn't. Adjust your approach based on evidence.`, timeline: "Month 4–5" },
    { title: "Scale & Sustain", description: `Expand your reach. Identify funding opportunities, grow your network, and create systems that make the effort sustainable long-term.`, timeline: `By ${timeline}` }
  ];

  const resources = [
    { icon: "🌐", title: "UN SDG Resources", detail: "sdgs.un.org — Official SDG targets, indicators, and progress reports." },
    { icon: "📖", title: "SDG Academy (edX)", detail: "Free online courses on sustainable development and the 2030 Agenda." },
    { icon: "🤝", title: "Local NGO & Civil Society", detail: "Connect with local NGOs aligned to SDG " + sdgId + " in your area for partnerships." },
    { icon: "💡", title: "Open SDG Data Hub", detail: "opensdg.org — Data tools and dashboards to track SDG progress globally." }
  ];

  return {
    sdgId,
    sdgName: name,
    goalSummary: goal.length > 80 ? goal.substring(0, 80) + "..." : goal,
    description: `A personalized roadmap aligned to SDG ${sdgId}: ${name}. Timeline: ${timeline}.`,
    steps,
    resources
  };
}
