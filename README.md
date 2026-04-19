# AI Service

Spring Boot starter project for an AI-assisted customer support platform.

## Current scope

- Ticket creation API
- Ticket list/detail API
- Java backend calling a separate AI analysis service over HTTP
- Local runtime with backend + MySQL + FastAPI AI mock service
- React frontend for ticket intake and queue inspection
- Dockerfile for container image build
- Kubernetes manifests for GKE deployment
- GitHub Actions CI/CD workflow for GKE deployment
- k6 load test scenario for API smoke/load testing

## Run locally

Start all services with Docker Compose:

```bash
docker compose up --build
```

Services:

- Backend: `http://localhost:8080`
- AI mock service: `http://localhost:8000`
- MySQL: `localhost:3308`
- React app: `http://localhost:5173`

For frontend-only development, run the React dev server separately:

```bash
cd frontend
npm install
npm run dev
```

Frontend:

- React app: `http://localhost:5173`

The backend now uses Flyway for schema migration. The initial ticket table is created from:

- `src/main/resources/db/migration/V1__create_tickets_table.sql`

`application.properties` is safe to commit. Local secrets should be provided through environment variables, Docker Compose, GitHub Secrets, or a local-only `.env` file copied from `.env.example`.

## API endpoints

- `GET /api/v1/system/status`
- `POST /api/v1/tickets`
- `GET /api/v1/tickets`
- `GET /api/v1/tickets/{id}`

Example request:

```bash
curl -X POST http://localhost:8080/api/v1/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "customerEmail": "customer@example.com",
    "subject": "Refund request",
    "content": "I want a refund for a duplicate payment.",
    "priority": "HIGH"
  }'
```

Example list request:

```bash
curl http://localhost:8080/api/v1/tickets
```

Example detail request:

```bash
curl http://localhost:8080/api/v1/tickets/1
```

Example validation error:

```bash
curl -X POST http://localhost:8080/api/v1/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "customerEmail": "bad-email",
    "subject": "",
    "content": "",
    "priority": "HIGH"
  }'
```

Expected error shape:

```json
{
  "timestamp": "2026-04-14T23:10:00+09:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Request validation failed",
  "path": "/api/v1/tickets",
  "details": [
    "customerEmail: must be a well-formed email address",
    "subject: must not be blank",
    "content: must not be blank"
  ]
}
```

## Kubernetes

GKE-oriented manifests are under `k8s/` and include:

- namespace
- configmap and secret
- mysql deployment, service, and pvc
- backend deployment, service, and hpa
- ai-mock-service deployment, service, and hpa
- frontend deployment, service, and hpa
- ingress

Apply in order:

```bash
cp k8s/secret.example.yaml k8s/secret.yaml
# edit k8s/secret.yaml before applying it
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -k k8s/
```

Do not commit `k8s/secret.yaml`. Commit only `k8s/secret.example.yaml`.

## CI/CD

GitHub Actions workflows are under `.github/workflows/`:

- `ci.yml`: backend build/test, frontend build, Docker image build checks
- `deploy-gke.yml`: build images, push to Artifact Registry, deploy to GKE, wait for rollout

GKE CI/CD setup details are documented in:

- `docs/gke-cicd.md`
- `docs/roadmap.md`

## Load test

`k6/ticket-flow.js` covers:

- status check
- ticket creation
- ticket listing

Run example:

```bash
k6 run -e BASE_URL=http://localhost:8080 k6/ticket-flow.js
```

## Verification note

`./gradlew test` has not been fully verified in this environment because Gradle wrapper distribution download is blocked by network restrictions.
`docker compose config` can still be used to validate Compose syntax locally.
