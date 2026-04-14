# VPA Installation & Troubleshooting — Week 13
## Vertical Pod Autoscaler on Minikube (Windows / Git Bash)

---

## Overview

VPA (Vertical Pod Autoscaler) is **not built into Kubernetes**. It must be installed manually. This document captures the complete installation journey, every issue encountered, the root cause, and the fix applied.

---

## Final Outcome

| What | Before VPA | After VPA |
|---|---|---|
| CPU Request | 100m (manual guess) | 25m (VPA observed actual usage) |
| Memory Request | 50Mi (manual guess) | 250Mi (VPA observed actual usage) |

VPA correctly identified that nginx was **over-provisioned on CPU** and **under-provisioned on memory**.

---

## Architecture Recap

VPA has 3 components:

| Component | Role |
|---|---|
| **Recommender** | Watches metrics, builds histogram, computes target/bounds |
| **Updater** | Evicts pods that drift too far from recommendations |
| **Admission Controller** | Mutating webhook — rewrites pod resource requests at creation time |

---

## Step-by-Step: What We Did

---

### STEP 1 — Install VPA

**Commands:**
```bash
git clone https://github.com/kubernetes/autoscaler.git
cd autoscaler/vertical-pod-autoscaler
./hack/vpa-up.sh
kubectl get pods -n kube-system | grep vpa
```

**Result:**
```
vpa-admission-controller   0/1   ContainerCreating   ← stuck
vpa-recommender            1/1   Running
vpa-updater                1/1   Running
```

---

### ISSUE 1 — Admission Controller stuck in ContainerCreating

**Debug command:**
```bash
kubectl describe pod -n kube-system -l app=vpa-admission-controller
```

**Error found:**
```
MountVolume.SetUp failed for volume "tls-certs": secret "vpa-tls-certs" not found
```

**RCA:** The TLS cert generation job (`vpa-generate-tls`) never ran. The secret `vpa-tls-certs` that the admission controller mounts as a volume did not exist. Without this secret the pod cannot start.

**Fix:** Manually run the cert generation script.

---

### STEP 2 — Run cert generation script manually

**Command:**
```bash
bash pkg/admission-controller/gencerts.sh
```

---

### ISSUE 2 — Git Bash path conversion error

**Error:**
```
req: subject name is expected to be in the format /type0=value0/...
This name is not in that format: 'C:/Program Files/Git/CN=vpa_webhook_ca'
```

**RCA:** Git Bash on Windows auto-converts paths starting with `/` to Windows absolute paths. The openssl `-subj "/CN=..."` flag was being converted to `C:/Program Files/Git/CN=...` which is invalid.

**Fix attempt:**
```bash
MSYS_NO_PATHCONV=1 bash pkg/admission-controller/gencerts.sh
```

---

### ISSUE 3 — /tmp/vpa-certs directory does not exist

**Error:**
```
genrsa: Can't open "/tmp/vpa-certs/caKey.pem" for writing, No such file or directory
```

**RCA:** The script tries to create `/tmp/vpa-certs` but Git Bash on Windows does not have a real `/tmp`. The `mkdir -p` inside the script silently fails.

**Fix:** Patch the script to use a local directory instead of `/tmp`:
```bash
sed -i 's|TMP_DIR="/tmp/vpa-certs"|TMP_DIR="$(pwd)/vpa-certs-tmp"|g' \
  pkg/admission-controller/gencerts.sh
```

Then pre-create the directory manually (because `mkdir -p` inside the script was still failing):
```bash
mkdir -p ./vpa-certs-tmp
MSYS_NO_PATHCONV=1 bash pkg/admission-controller/gencerts.sh
```

**Result:** Certs generated successfully in `./vpa-certs-tmp/`:
```
caCert.pem  caKey.pem  serverCert.pem  serverKey.pem  server.conf  server.csr
```

---

### STEP 3 — Upload certs to cluster as a secret

**Command:**
```bash
kubectl create secret --namespace=kube-system generic vpa-tls-certs \
  --from-file=./vpa-certs-tmp/caKey.pem \
  --from-file=./vpa-certs-tmp/caCert.pem \
  --from-file=./vpa-certs-tmp/serverKey.pem \
  --from-file=./vpa-certs-tmp/serverCert.pem
```

**Issue — secret already existed from a previous failed attempt:**
```
error: failed to create secret — secrets "vpa-tls-certs" already exists
```

**Fix:**
```bash
kubectl delete secret -n kube-system vpa-tls-certs
kubectl create secret --namespace=kube-system generic vpa-tls-certs \
  --from-file=./vpa-certs-tmp/caKey.pem \
  --from-file=./vpa-certs-tmp/caCert.pem \
  --from-file=./vpa-certs-tmp/serverKey.pem \
  --from-file=./vpa-certs-tmp/serverCert.pem
```

---

### STEP 4 — Restart admission controller

```bash
kubectl delete pod -n kube-system -l app=vpa-admission-controller
kubectl get pods -n kube-system | grep vpa
```

**Result:** All 3 VPA pods now `1/1 Running`. ✅

---

### STEP 5 — Verify VPA CRDs and create test deployment

```bash
kubectl get crd | grep verticalpodautoscaler
kubectl get vpa   # should return "No resources found" — correct!

# Deploy test app
kubectl create deployment vpa-demo --image=nginx --replicas=2
kubectl set resources deployment vpa-demo --requests=cpu=100m,memory=50Mi

# Create VPA in Off mode first (recommendations only)
cat <<EOF | kubectl apply -f -
apiVersion: autoscaling.k8s.io/v1
kind: VerticalPodAutoscaler
metadata:
  name: vpa-demo
spec:
  targetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: vpa-demo
  updatePolicy:
    updateMode: "Off"
EOF
```

---

### STEP 6 — Wait for recommendations

After ~5 minutes:
```bash
kubectl describe vpa vpa-demo
```

**Recommendations received:**
```
Container: nginx
  Lower Bound:  cpu: 25m    memory: 250Mi
  Target:       cpu: 25m    memory: 250Mi
  Upper Bound:  cpu: 100G   memory: ~90TB  ← no maxAllowed set!
```

**Insight:**
- CPU was **over-provisioned** — nginx barely uses CPU. Wasting 75m per pod.
- Memory was **under-provisioned** — nginx needs 250Mi, not 50Mi. Would OOMKill in production.
- Upper bound is huge because we didn't set `maxAllowed`. Always set this in production.

---

### STEP 7 — Switch VPA to Auto mode

```bash
kubectl patch vpa vpa-demo --type='merge' \
  -p '{"spec":{"updatePolicy":{"updateMode":"Auto"}}}'
```

**Delete pods to trigger mutation:**
```bash
kubectl delete pod -l app=vpa-demo
```

**Check new pod resources:**
```bash
kubectl get pod <pod-name> -o yaml | grep -A 4 resources
```

**Result:** Still `cpu: 100m, memory: 50Mi` — VPA not mutating yet.

---

### ISSUE 4 — VPA Admission Controller not mutating pods

**Debug:**
```bash
kubectl logs -n kube-system -l app=vpa-admission-controller --tail=30
```

**Error in logs:**
```
http: TLS handshake error from 10.244.0.1:xxxxx: remote error: tls: bad certificate
```

**RCA:** `Failure Policy: Ignore` is set on the webhook — meaning if the TLS handshake fails, Kubernetes silently skips mutation and uses the original pod spec. The webhook config's `caBundle` was from an older cert, and the `serverCert.pem` had a **missing Subject Alternative Name (SAN)**. Modern Kubernetes (1.19+) rejects certificates that only have a CN and no SAN. Every webhook call was being rejected at the TLS layer before even reaching the admission controller handler.

**Debug commands used:**
```bash
kubectl get mutatingwebhookconfigurations | grep vpa
kubectl describe mutatingwebhookconfiguration vpa-webhook-config
kubectl get svc -n kube-system vpa-webhook
kubectl get endpoints -n kube-system vpa-webhook

# Check what SANs are in the server cert
openssl x509 -in ./vpa-certs-tmp/serverCert.pem -text -noout | grep -A 3 "Subject Alternative"
```

**Finding:** The `grep` returned empty — **no SAN in the server cert at all**.

---

### ISSUE 5 — Regenerating cert with SAN on old OpenSSL

**Attempt 1 — `-addext` flag:**
```bash
MSYS_NO_PATHCONV=1 openssl x509 -req \
  -in ./vpa-certs-tmp/server.csr \
  -CA ./vpa-certs-tmp/caCert.pem \
  -CAkey ./vpa-certs-tmp/caKey.pem \
  -CAcreateserial \
  -out ./vpa-certs-tmp/serverCert.pem \
  -days 100000 \
  -addext "subjectAltName=DNS:vpa-webhook.kube-system.svc"
```

**Error:**
```
x509: Extra (unknown) options: "addext" ...
```

**RCA:** Git Bash ships with an older OpenSSL that doesn't support `-addext` (added in OpenSSL 1.1.1).

**Fix — use standalone ext file:**
```bash
# Create ext file
cat > ./vpa-certs-tmp/san.ext << 'EOF'
subjectAltName=DNS:vpa-webhook.kube-system.svc
EOF

# Regenerate cert with SAN via extfile
MSYS_NO_PATHCONV=1 openssl x509 -req \
  -in ./vpa-certs-tmp/server.csr \
  -CA ./vpa-certs-tmp/caCert.pem \
  -CAkey ./vpa-certs-tmp/caKey.pem \
  -CAcreateserial \
  -out ./vpa-certs-tmp/serverCert.pem \
  -days 100000 \
  -extfile ./vpa-certs-tmp/san.ext

# Verify SAN is present
openssl x509 -in ./vpa-certs-tmp/serverCert.pem -text -noout | grep -A 3 "Subject Alternative"
```

**Output:**
```
X509v3 Subject Alternative Name:
    DNS:vpa-webhook.kube-system.svc
```

---

### STEP 8 — Upload new cert and restart

```bash
kubectl delete secret -n kube-system vpa-tls-certs

kubectl create secret --namespace=kube-system generic vpa-tls-certs \
  --from-file=./vpa-certs-tmp/caKey.pem \
  --from-file=./vpa-certs-tmp/caCert.pem \
  --from-file=./vpa-certs-tmp/serverKey.pem \
  --from-file=./vpa-certs-tmp/serverCert.pem

kubectl rollout restart deployment -n kube-system vpa-admission-controller
```

---

### STEP 9 — Verify mutation is working

**Admission controller logs:**
```
"Admitting pod" pod="default/vpa-demo-bf54cfd5f-%"
"Sending patches" patches=[
  {"op":"add","path":"/spec/containers/0/resources/requests/memory","value":"250Mi"},
  {"op":"add","path":"/spec/containers/0/resources/requests/cpu","value":"25m"},
  {"op":"add","path":"/metadata/annotations/vpaUpdates",
   "value":"Pod resources updated by vpa-demo: container 0: memory request, cpu request"}
]
```

**Pod verification:**
```bash
kubectl get pod <pod-name> -o yaml | grep -A 4 resources
```

**Result:**
```yaml
resources:
  requests:
    cpu: 25m       ← VPA injected (was 100m)
    memory: 250Mi  ← VPA injected (was 50Mi)
```

**VPA working end-to-end!** ✅

---

## Issues Summary Table

| # | Issue | RCA | Fix |
|---|---|---|---|
| 1 | Admission controller stuck in ContainerCreating | `vpa-tls-certs` secret missing — cert gen job never ran | Run `gencerts.sh` manually |
| 2 | `gencerts.sh` fails with wrong path format | Git Bash converts `/CN=...` to `C:/Program Files/Git/CN=...` | `MSYS_NO_PATHCONV=1` prefix |
| 3 | `genrsa: Can't open /tmp/vpa-certs/caKey.pem` | Git Bash has no real `/tmp` | Patch script to use `$(pwd)/vpa-certs-tmp`, pre-create dir manually |
| 4 | Pods not being mutated despite VPA in Auto mode | TLS handshake failing — server cert had no SAN | Regenerate server cert with SAN using `-extfile` |
| 5 | `-addext` flag not recognized | Git Bash ships with old OpenSSL that lacks `-addext` | Use standalone `san.ext` file with `-extfile` flag |

---

## Key Learnings

**1. VPA Failure Policy is `Ignore`**
If the admission webhook is unreachable or TLS fails, Kubernetes silently skips mutation. No error is shown on the pod. Always check admission controller logs when VPA seems inactive.

**2. Modern K8s requires SAN in TLS certs**
Since Kubernetes 1.19, certificates must have a Subject Alternative Name. CN alone is not sufficient. The cert must include `DNS:vpa-webhook.kube-system.svc`.

**3. Git Bash on Windows has two quirks**
- Path conversion: `/CN=` becomes `C:/Program Files/Git/CN=` → fix with `MSYS_NO_PATHCONV=1`
- No real `/tmp` directory → patch scripts to use relative paths

**4. The Deployment manifest never changes**
VPA mutates the **pod spec** at creation time via the Admission Controller webhook. Your `Deployment` yaml still shows the original values. Always inspect the running pod with `kubectl get pod -o yaml` to see actual injected values.

**5. Off mode is safe for analysis**
Always start with `updateMode: "Off"` to observe recommendations before letting VPA make changes. Only switch to `Auto` when you trust the recommendations.
