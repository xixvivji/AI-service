# Project Hardening Roadmap

## Application

- Add ticket status transition API.
- Add pagination and sorting for ticket list queries.
- Add structured logging with request IDs.
- Add integration tests with Testcontainers.
- Add API documentation with OpenAPI.

## AI Service

- Replace `ai-mock-service` with a real AI service boundary.
- Add timeout, retry, and circuit breaker policies.
- Store AI analysis attempts separately for observability.
- Add prompt/version metadata once real model calls are introduced.

## Database

- Use Flyway for every schema change.
- Add indexes for list query access patterns.
- Move from in-cluster MySQL to Cloud SQL for GKE.
- Add backup and restore practice.

## Kubernetes

- Split manifests by environment.
- Add PodDisruptionBudget for backend and frontend.
- Add resource tuning after k6 test results.
- Add managed TLS certificate and static IP.
- Add Cloud SQL Auth Proxy or Workload Identity integration if using Cloud SQL.

## CI/CD

- Add deployment smoke tests after rollout.
- Add branch protection requiring CI.
- Add separate manual approval for production.
- Add image scanning and SBOM generation.
- Add rollback runbook.

## Load Testing

- Add multiple k6 scenarios for read-heavy and write-heavy traffic.
- Run tests against GKE Ingress after each major deployment change.
- Track p95 latency, error rate, CPU, memory, and HPA scaling behavior.
