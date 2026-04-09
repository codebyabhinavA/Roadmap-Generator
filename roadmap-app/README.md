# RoadMap — Goal to Action Planner

A Spring Boot web app that uses Claude AI to turn any goal into a personalised roadmap.
Aligned with **SDG 17: Partnership for Goals**.

---

## Project Structure

```
roadmap-app/
├── pom.xml
├── render.yaml
├── src/
│   └── main/
│       ├── java/com/roadmap/
│       │   ├── RoadmapApplication.java
│       │   ├── controller/RoadmapController.java
│       │   ├── model/ApiModels.java
│       │   └── service/ClaudeService.java
│       └── resources/
│           ├── application.properties
│           └── static/
│               └── index.html
```

---

## Prerequisites

- Java 17+
- Maven 3.8+
- Git
- A free Render account: https://render.com
- An Anthropic API key: https://console.anthropic.com

---

## Step-by-Step: Deploy to Render via Git

### 1. Set up Git locally

```bash
cd roadmap-app
git init
git add .
git commit -m "Initial commit: RoadMap app"
```

### 2. Create a GitHub repository

Go to https://github.com/new, create a new repository (e.g. `roadmap-app`), then:

```bash
git remote add origin https://github.com/YOUR_USERNAME/roadmap-app.git
git branch -M main
git push -u origin main
```

### 3. Deploy on Render

1. Go to https://render.com and sign in
2. Click **"New +"** → **"Web Service"**
3. Connect your GitHub account and select the `roadmap-app` repository
4. Render will auto-detect the `render.yaml` file — click **"Apply"**
5. Under **Environment Variables**, add:
   - Key: `ANTHROPIC_API_KEY`
   - Value: your key from https://console.anthropic.com
6. Click **"Create Web Service"**

Render will build and deploy automatically. Your app will be live at:
`https://roadmap-app.onrender.com`

---

## Run Locally

```bash
# Set your API key
export ANTHROPIC_API_KEY=sk-ant-...

# Build and run
mvn clean package -DskipTests
java -jar target/roadmap-app-0.0.1-SNAPSHOT.jar

# Open in browser
open http://localhost:8080
```

---

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/questions` | Generate follow-up questions for a goal |
| POST | `/api/roadmap` | Generate full roadmap from goal + answers |

### Example: Generate Questions
```json
POST /api/questions
{ "goal": "Become a data scientist" }
```

### Example: Generate Roadmap
```json
POST /api/roadmap
{
  "goal": "Become a data scientist",
  "answers": [
    { "question": "What is your current skill level?", "answer": "Beginner, know basic Python" },
    { "question": "How many hours per week can you dedicate?", "answer": "10 hours" }
  ]
}
```

---

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2, Maven
- **AI**: Anthropic Claude API (claude-sonnet-4-20250514)
- **Frontend**: Vanilla HTML/CSS/JS (served as static files)
- **Hosting**: Render (free tier)
