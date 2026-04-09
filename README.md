# 🌍 SDG Roadmap Generator

A Java Spring Boot web application that helps users create personalised action roadmaps aligned to the UN Sustainable Development Goals (SDGs).

---

## 📁 Project Structure

```
sdg-roadmap/
├── frontend/
│   ├── index.html        ← Main UI (3-step flow)
│   ├── style.css         ← All styles
│   └── app.js            ← Frontend logic & API calls
│
└── backend/
    ├── pom.xml           ← Maven dependencies
    └── src/
        └── main/
            ├── java/com/sdg/roadmap/
            │   ├── SdgRoadmapApplication.java     ← Spring Boot entry point
            │   ├── controller/
            │   │   └── RoadmapController.java     ← REST API  (POST /api/roadmap)
            │   ├── service/
            │   │   └── RoadmapService.java        ← Roadmap generation logic
            │   └── model/
            │       ├── RoadmapRequest.java        ← Incoming JSON DTO
            │       └── RoadmapResponse.java       ← Outgoing JSON DTO
            └── resources/
                ├── application.properties         ← Config (port, logging)
                └── static/                        ← ⬅ Copy frontend files HERE
                    ├── index.html
                    ├── style.css
                    └── app.js
```

---

## 🚀 How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps

1. **Copy the frontend files** into `backend/src/main/resources/static/`:
   ```
   cp frontend/* backend/src/main/resources/static/
   ```

2. **Build and run the backend**:
   ```bash
   cd backend
   mvn spring-boot:run
   ```

3. **Open your browser** at:
   ```
   http://localhost:8080
   ```

That's it! Spring Boot serves the frontend and the API from the same server.

---

## 🔌 API Reference

### POST `/api/roadmap`

**Request Body:**
```json
{
  "goal": "I want to start a community garden to reduce food waste",
  "sdgId": "2",
  "answers": {
    "timeline": "6–12 months",
    "resources": "Some knowledge/skills",
    "experience": "Intermediate",
    "location": "Thiruvananthapuram, Kerala"
  }
}
```

**Response:**
```json
{
  "sdgId": "2",
  "sdgName": "Zero Hunger",
  "goalSummary": "I want to start a community garden...",
  "description": "A personalised roadmap aligned to SDG 2...",
  "steps": [
    { "title": "Define & Research Your Goal", "description": "...", "timeline": "Week 1–2" },
    ...
  ],
  "resources": [
    { "icon": "🌐", "title": "UN SDG Official Portal", "detail": "sdgs.un.org..." },
    ...
  ]
}
```

### GET `/api/health`
Returns `200 OK` with "SDG Roadmap API is running ✅"

---

## 🧩 SDGs with Specialised Roadmaps

| SDG | Name | Custom Steps |
|-----|------|-------------|
| 1   | No Poverty | ✅ |
| 3   | Good Health & Well-being | ✅ |
| 4   | Quality Education | ✅ |
| 13  | Climate Action | ✅ |
| All others | Generic SDG roadmap | ✅ |

---

## 💡 Possible Enhancements

- Integrate Claude / OpenAI API for AI-generated roadmap text
- Add user authentication and save roadmaps to a database (H2 / PostgreSQL)
- Export roadmap as PDF using iText or Apache PDFBox
- Add a progress tracker for each roadmap step
- Show SDG badges and icons from the UN brand assets

---

## 🛠 Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | HTML5, CSS3, Vanilla JS |
| Backend | Java 17, Spring Boot 3.2 |
| Build | Maven |
| Fonts | Google Fonts (Playfair Display, DM Sans) |
