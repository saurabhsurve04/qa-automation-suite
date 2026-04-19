# Week 13 Handoff — Resume at RemoteWebDriver

## Who I am
- **Saurabh** — QA Lead, 15 years experience, based in India
- Working toward Senior/Lead QA role at a new org
- **GitHub:** saurabhsurve04 | **Repo:** qa-automation-suite | **Package:** com.saurabh.projects.javadrills
- Environment: Windows, PowerShell, Git Bash, Minikube, Docker Desktop
- Studies ~1-2 hrs evenings, more on weekends

---

## Capstone Project — TestGenie
AI-powered tool that generates BDD Gherkin scenarios from user stories.
Stack: Java, Spring Boot, Claude/OpenAI APIs, Docker, Kubernetes.
Goal: Deploy as `java -jar testgenie.jar` inside a Kubernetes Pod.

---

## Overall Roadmap — Current Position
18-week self-directed roadmap. Currently on **Week 13**.

| Phase | Status |
|---|---|
| Weeks 1–8: Java, Selenium, Rest Assured | ✅ Complete |
| Weeks 9–11: Docker, GitHub Actions, Portfolio | ✅ Complete |
| Week 12: Kubernetes basics (Deployments, Services, ConfigMaps, kubectl) | ✅ Complete |
| **Week 13: Advanced Kubernetes + Selenium Grid + Docker Compose** | 🔄 In Progress |
| Weeks 14–18: OpenShift, GitHub Actions CI/CD, Spring Boot, AI API, TestGenie deploy | ⏳ Pending |

---

## Week 13 — What's Done

### VPA (Vertical Pod Autoscaler) — COMPLETED ✅
Fully hands-on. All Windows/Minikube gotchas resolved. See previous handoff for details.

### Selenium Grid — Concepts COMPLETED ✅

**Core problem Grid solves:**
- Parallel test execution across multiple browsers
- Cross-browser testing without physical hardware (e.g. Safari on macOS without owning a Mac)
- A junior QA's naive alternative: running multiple threads locally — doesn't solve cross-browser, limited by local hardware

**Grid 3 — Hub + Node architecture:**
- Hub is single entry point — receives test requests with **Capabilities** (browserName, browserVersion, platformName)
- Hub maintains node registry + session mappings + routing — all in one process
- Single point of failure — hub crash = node registry lost + active sessions lost + full system collapse

**Grid 4 — Decomposed architecture:**

| Component | Responsibility |
|---|---|
| Router | Entry point — receives incoming session requests |
| Distributor | Owns node registry — knows all nodes, slots, availability |
| Session Map | Owns session mappings — tracks which session is on which node |

**Why Grid 4 is better at scale:**
- Isolated failure — if Session Map crashes, nodes stay registered at Distributor, Router still receives requests
- In Grid 3, one process going down = total collapse
- In Grid 4, blast radius is limited to the failed component only

**Capabilities:** The metadata a test sends to the hub/router to get routed to the correct node — browserName, browserVersion, platformName. Hub matches capabilities to available nodes.

**Node registration:** Nodes use HUB_HOST/HUB_PORT (Grid 3) or SE_EVENT_BUS_* (Grid 4) to register themselves with the hub at startup. Hub never finds nodes — nodes find the hub.

---

### Docker Compose — COMPLETED ✅

**What Docker Compose solves:**
- Multiple containers defined in one file, started with one command
- Automatic shared network — containers reach each other by **service name** as hostname
- Replaces running multiple `docker run` commands manually

**Key concepts learned:**
- `services:` — each entry is one container
- Service name = hostname on the internal network
- `depends_on:` — controls startup order (container start, not readiness)
- `shm_size: "2g"` — mandatory for browser containers; default 64MB causes silent crashes
- VNC port (5900) belongs on browser nodes, not hub — hub runs no browser
- Nodes do NOT need external ports — tests only talk to hub, never directly to nodes
- `container_name:` — if used, SE_EVENT_BUS_HOST must match it exactly

**Grid 3 compose attempt — failed:**
- Used `selenium/hub:3.141.59` + node images same version
- `NullPointerException` in `fixUpCapabilities` during node registration
- Root cause: hub and node images from different build dates — capability mismatch
- Fix would be date-pinned tags e.g. `3.141.59-20210929` across all three
- Decision: moved to Grid 4 instead (correct choice for modern stack)

**Final working Grid 4 compose file:**
```yaml
services:
  selenium-hub:
    image: selenium/hub:4.1.0
    container_name: selenium-hub
    ports:
      - "4442:4442"
      - "4443:4443"
      - "4444:4444"

  chrome:
    image: selenium/node-chrome:4.1.0
    shm_size: "2g"
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
    ports:
      - "5901:5900"

  firefox:
    image: selenium/node-firefox:4.1.0
    shm_size: "2g"
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
    ports:
      - "5902:5900"
```

**Grid 4 ports:**
- `4444` — where test code connects
- `4442` — event bus publish port (nodes register via this)
- `4443` — event bus subscribe port (nodes receive messages via this)
- `5555` — internal node port, baked into image, never needs external exposure

**Confirmed working:**
- All 3 containers running: `docker compose ps` shows Up status
- Hub logs show: `LocalDistributor.add` — both nodes registered
- Both nodes switched from `DOWN to UP` after health check handshake
- Grid UI accessible at `http://localhost:4444` (root — not /grid/console like Grid 3)
- Console shows: hub version, 2 registered nodes, Chrome (Linux), Firefox (Linux)

**One outstanding item from this session:**
- Exact Chrome and Firefox version numbers NOT confirmed — Saurabh took a break before reading them off the console
- **Must confirm these at start of next session** before writing RemoteWebDriver capabilities

**File location:** `selenium-grid/docker-compose.yml` on branch `week-13-advanced-kubernetes-selenium-grid-docker-compose`
- **TODO: Commit this file before next session**

---

## Week 13 — What's Next (START HERE)

### Step 1 — Confirm browser versions (2 minutes)
Run `docker compose up -d` and open `http://localhost:4444`
Read exact Chrome and Firefox version numbers off the console.

### Step 2 — RemoteWebDriver ⬅️ RESUME HERE
- What RemoteWebDriver is and how it differs from ChromeDriver
- Writing a test that connects to Grid at `http://localhost:4444`
- Setting capabilities (DesiredCapabilities / ChromeOptions)
- Running same test on Chrome AND Firefox by changing one line
- Confirming test appears as active session on Grid console

### Step 3 — Scaling nodes
- `docker compose up --scale chrome=3`
- What happens to available slots on Grid console
- Why this matters for parallel test execution

### Step 4 — Grid on Kubernetes
- Deploying hub + nodes as Kubernetes Pods
- Connects back to Week 12 Kubernetes knowledge
- Direct connection to TestGenie deployment model

---

## Key Patterns & Flags for Claude

### Working Style — MUST maintain
- **Never give code directly** — push Saurabh to write it first
- **Push back** when "done" or "got it" appears without evidence
- **Require predictions** before executing commands
- **Demand scenario-grounded answers**, not textbook definitions
- **Call out immediately** if answers look AI-generated or Googled without acknowledgment
- Concepts explained before implementation
- Connect everything to TestGenie or real org usage
- Honest feedback over validation

### Known patterns to watch
- Tends toward surface-level answers under initial prompting — requires follow-up questions
- Occasionally skips sub-questions when answering — call this out explicitly
- Strong log-reading skills demonstrated this session (correctly identified NullPointerException root cause)
- Will say "no idea" to avoid reasoning — push back, scaffold with guiding questions

---

## Parallel Track — Java Drills
Days 1–5 completed: ArrayList, LinkedHashMap, HashSet, TreeMap, Stream-based sorting.
Recurring bug: forgetting `public` on `main` method.
Days 6–30 remaining — resume when Week 13 hands-on is complete.

---

## Kubernetes Knowledge So Far
- Deployments, Services, ConfigMaps from memory
- Debugged ImagePullBackOff
- kubectl logs / --previous
- ConfigMap via envFrom, verified with kubectl exec
- HPA + VPA concepts and hands-on
- `kubectl apply` vs `kubectl create` — **STILL OUTSTANDING, answer required before Week 14**

## Git Workflow
feature branch → local checkout → dev → PR → squash merge to main
Current branch: `week-13-advanced-kubernetes-selenium-grid-docker-compose`

---

## Tools
Java, Spring Boot, TestNG, Selenium WebDriver, Rest Assured, Cucumber/BDD, Maven,
Docker Desktop, Minikube (Windows), kubectl, GitHub Actions (planned), OpenShift (planned),
Claude API, OpenAI API, Git Bash + PowerShell
