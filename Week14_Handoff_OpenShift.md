# Week 14 Handoff — Resume at OpenShift

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
**Note:** TestGenie pods already running in Minikube cluster alongside Selenium Grid.

---

## Overall Roadmap — Current Position
18-week self-directed roadmap. Currently moving to **Week 14**.

| Phase | Status |
|---|---|
| Weeks 1–8: Java, Selenium, Rest Assured | ✅ Complete |
| Weeks 9–11: Docker, GitHub Actions, Portfolio | ✅ Complete |
| Week 12: Kubernetes basics (Deployments, Services, ConfigMaps, kubectl) | ✅ Complete |
| Week 13: Advanced Kubernetes + Selenium Grid + Docker Compose | ✅ Complete |
| **Week 14: OpenShift basics** | 🔄 Next |
| Weeks 15–18: GitHub Actions CI/CD, Spring Boot, AI API, TestGenie deploy | ⏳ Pending |

---

## Week 13 — What Was Completed

### VPA (Vertical Pod Autoscaler) — COMPLETED ✅
Fully hands-on. Completed in previous session.

### Docker Compose + Selenium Grid 4 — COMPLETED ✅

**Final working compose file:**
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

  firefox:
    image: selenium/node-firefox:4.1.0
    shm_size: "2g"
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
```

**Key learnings:**
- VNC ports (5900) removed from chrome service — nodes don't need external port exposure for tests
- `--scale chrome=3` confirmed working after VNC port fix
- Grid UI at `http://localhost:4444` (not `/grid/console` like Grid 3)
- Browser versions: Chrome v96.0, Firefox v95.0 (Linux nodes)

### RemoteWebDriver — COMPLETED ✅

**Working test:** `src/test/java/com/saurabh/projects/grid/GridSmokeTest.java`

Key points proven:
- `ChromeOptions` → routes to Chrome node, `FirefoxOptions` → routes to Firefox node
- Grid 4 URL is `http://localhost:4444` (not `/wd/hub` — that's Grid 3)
- `ChromeOptions` already carries `browserName=chrome`, no need for `setCapability`
- Session appeared on Grid console with session ID during test execution
- Cross-browser with one line change — core value of RemoteWebDriver proven hands-on

### Selenium Grid on Kubernetes — COMPLETED ✅

**Three YAML files written from memory and applied:**

`selenium-hub-deployment.yaml` — standard Deployment, image `selenium/hub:4.1.0`, ports 4444/4442/4443

`selenium-hub-service.yaml` — ClusterIP Service, named ports (listener/publisher/subscriber), makes `selenium-hub` resolvable by DNS inside cluster

`chrome-node-deployment.yaml` — 3 replicas, `SE_EVENT_BUS_*` env vars pointing at `selenium-hub`, shared memory via `emptyDir` volume:
```yaml
volumeMounts:
  - mountPath: /dev/shm
    name: dshm
volumes:
  - name: dshm
    emptyDir:
      medium: Memory
      sizeLimit: 2Gi
```

**Key learnings:**
- Multi-port Service requires `name:` on each port entry
- `shm_size` (Docker Compose) → `emptyDir` with `medium: Memory` (Kubernetes)
- Node retry loop: nodes send registration events every ~10s until hub is reachable
- Service DNS makes hub reachable by name regardless of Pod IP changes

**Verified working:**
- All 3 Chrome nodes switched from DOWN to UP in hub logs
- `kubectl get pods` — all Running, 0 restarts
- `kubectl get svc` — selenium-hub ClusterIP exposing 4444,4442,4443

---

## Outstanding Items

- **Firefox node Deployment YAML** — not written yet. Same pattern as chrome-node-deployment.yaml, image `selenium/node-firefox:4.1.0`. Worth writing from memory before Week 14 starts.
- **Java Drills Days 6–30** — paused during Week 13 hands-on. Resume after Week 14 is stable.

---

## Git State
- Branch `week-13-advanced-kubernetes-selenium-grid-docker-compose` — squash merged to main ✅
- New branch for Week 14: `week-14-openshift` (to be created at session start)
```bash
git checkout main
git pull
git checkout -b week-14-openshift
```

---

## Kubernetes Knowledge Accumulated
- Deployments, Services, ConfigMaps from memory
- Debugged ImagePullBackOff
- kubectl logs / --previous
- ConfigMap via envFrom, verified with kubectl exec
- HPA + VPA concepts and hands-on
- `kubectl apply` vs `kubectl create` — apply creates or updates, create fails if exists ✅
- Multi-container orchestration via Deployments (Selenium Grid on K8s)
- Service DNS resolution inside cluster

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
- Tends toward surface-level answers under initial prompting — requires follow-up
- Occasionally skips sub-questions — call this out explicitly
- Strong log-reading skills — correctly diagnoses from output
- Will say "no idea" to avoid reasoning — push back, scaffold with guiding questions
- Recurring bug: forgetting `public` on `main` method in Java drills

---

## Tools
Java, Spring Boot, TestNG, Selenium WebDriver, Rest Assured, Cucumber/BDD, Maven,
Docker Desktop, Minikube (Windows), kubectl, Kubernetes,
GitHub Actions (planned), OpenShift (planned),
Claude API, OpenAI API, Git Bash + PowerShell
