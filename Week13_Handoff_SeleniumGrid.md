# Week 13 Handoff — Resume at Selenium Grid

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

**What VPA does:** Automatically right-sizes pod CPU/memory requests based on actual observed usage. Solves the problem of humans guessing resource requests wrong.

**3 Components:**
- **Recommender** — watches metrics, builds histogram, computes target/bounds
- **Updater** — evicts pods that drift too far from recommendations (respects PDBs)
- **Admission Controller** — mutating webhook, rewrites pod resource requests at creation time

**Update Modes:** Off (recommendations only) → Initial (apply at creation only) → Recreate (evict + recreate) → Auto (same as Recreate currently)

**Key learnings from hands-on:**
- VPA is NOT built into Kubernetes — must install manually
- `Failure Policy: Ignore` on webhook = silent skip if TLS fails. Always check admission controller logs if VPA seems inactive
- Modern K8s requires SAN in TLS certs (not just CN)
- Deployment manifest never changes — VPA mutates the running pod spec. Always check `kubectl get pod -o yaml` for actual values
- VPA + HPA cannot both scale on CPU/memory — they fight. Correct pattern: VPA for right-sizing + HPA on custom metrics
- Always set `maxAllowed` in resourcePolicy — without it, Upper Bound becomes absurdly large

**Hands-on result:**
```
Deployment set:   cpu: 100m  memory: 50Mi  (manual guess)
VPA recommended:  cpu: 25m   memory: 250Mi (actual observed)
```
nginx was over-provisioned on CPU and under-provisioned on memory. VPA caught both.

**Installation on Windows/Minikube gotchas (all resolved):**
1. Secret `vpa-tls-certs` missing → cert gen job never ran → ran `gencerts.sh` manually
2. Git Bash path conversion breaking openssl → fixed with `MSYS_NO_PATHCONV=1`
3. No real `/tmp` on Git Bash → patched script to use `$(pwd)/vpa-certs-tmp`
4. Server cert had no SAN → TLS handshake silently failing → regenerated with `-extfile san.ext`
5. Old OpenSSL on Git Bash → no `-addext` support → used standalone ext file

---

## Week 13 — What's Next (START HERE)

### Selenium Grid — NOT STARTED ⬅️ RESUME HERE

Topics to cover:
- What problem Selenium Grid solves (parallel cross-browser testing)
- Hub + Node architecture (Grid 3) vs Router/Distributor/SessionMap (Grid 4)
- Running Grid with Docker Compose (standalone, hub-node)
- Connecting Selenium tests to Grid (RemoteWebDriver)
- Scaling nodes
- Grid on Kubernetes (deploying hub + nodes as pods) — connects to TestGenie

### Docker Compose — NOT STARTED
- Multi-container apps with docker-compose.yml
- Services, networks, volumes
- Running Selenium Grid with Docker Compose
- Relevant to TestGenie (Spring Boot app + dependencies as compose services)

---

## Working Style — IMPORTANT for Claude to maintain

- **Never give code directly** — push Saurabh to write it first
- **Push back** when "done" or "got it" appears without evidence (show code + output)
- **Require predictions** before executing commands
- **Demand scenario-grounded answers**, not textbook definitions
- **Carry forward** unanswered questions and require answers before proceeding
- Concepts explained before implementation
- Connect everything to TestGenie or real org usage
- Honest feedback over validation — push back when corners are cut

---

## Parallel Track — Java Drills (Days 1–5 done)
Days 1–5 completed: ArrayList, LinkedHashMap, HashSet, TreeMap, Stream-based sorting.
Recurring bug: forgetting `public` on `main` method — watch for this.
Days 6–30 remaining: OOP design, Functional interfaces, Streams, Strings, Exceptions, Advanced patterns.

---

## Kubernetes Knowledge So Far
- Wrote Deployment and Service YAMLs from memory
- Debugged ImagePullBackOff
- kubectl logs / --previous
- ConfigMap via envFrom, verified with `kubectl exec -- env | Select-String`
- HPA concepts (covered before VPA)
- VPA — fully hands-on completed this session

## Git Workflow
feature branch → local checkout → dev → PR → squash merge to main
Current branch: `week-13-advanced-kubernetes-selenium-grid-docker-compose`

---

## Tools
Java, Spring Boot, TestNG, Selenium WebDriver, Rest Assured, Cucumber/BDD, Maven,
Docker Desktop, Minikube (Windows), kubectl, GitHub Actions (planned), OpenShift (planned),
Claude API, OpenAI API, Git Bash + PowerShell
