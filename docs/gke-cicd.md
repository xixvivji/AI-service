# GKE CI/CD Setup

This project uses GitHub Actions for CI and GKE deployment.

## Pipeline Shape

CI runs on pull requests and pushes to `main`:

- Backend Gradle test and bootJar build
- Frontend npm build
- Docker image build checks for backend, frontend, and AI mock service

CD runs on pushes to `main` and manual dispatch:

- Authenticate to Google Cloud with Workload Identity Federation
- Build and push three Docker images to Artifact Registry
- Apply Kubernetes manifests
- Update GKE deployment images
- Wait for rollout completion

## Required GitHub Variables

Add these as repository or environment variables:

- `GCP_PROJECT_ID`
- `GCP_WORKLOAD_IDENTITY_PROVIDER`
- `GCP_SERVICE_ACCOUNT`
- `GKE_CLUSTER`
- `GKE_LOCATION`
- `ARTIFACT_REGISTRY_LOCATION`
- `ARTIFACT_REGISTRY_REPOSITORY`

Example image path format:

```text
asia-northeast3-docker.pkg.dev/<project-id>/<repository>/ai-service-backend:<git-sha>
```

## Required GitHub Secrets

Add these as repository or production environment secrets:

- `DB_USERNAME`
- `DB_PASSWORD`
- `MYSQL_DATABASE`
- `MYSQL_USER`
- `MYSQL_PASSWORD`
- `MYSQL_ROOT_PASSWORD`

For the current local defaults:

```text
DB_USERNAME=ai_user
MYSQL_DATABASE=ai_service
MYSQL_USER=ai_user
```

Use stronger values for passwords in real GKE.

Do not commit `k8s/secret.yaml`. Use `k8s/secret.example.yaml` as a template for local manual testing only. GitHub Actions creates the real Kubernetes Secret from GitHub Secrets during deployment.

## GCP Resources Needed

- GKE cluster
- Artifact Registry Docker repository
- Workload Identity Federation provider for GitHub
- Google service account with deployment permissions

The service account needs enough permissions to:

- Push images to Artifact Registry
- Read GKE cluster credentials
- Apply Kubernetes manifests

For a personal project, start narrow but practical:

- `roles/artifactregistry.writer`
- `roles/container.developer`

## Manual Deploy Check

After CD runs, check:

```bash
kubectl get pods -n ai-service
kubectl get svc -n ai-service
kubectl get ingress -n ai-service
kubectl rollout status deployment/ai-service-backend -n ai-service
kubectl rollout status deployment/ai-service-frontend -n ai-service
```

## Next Hardening Items

- Replace in-cluster MySQL with Cloud SQL before production-like testing.
- Add managed certificate and domain for GKE Ingress.
- Add separate `dev` and `prod` namespaces.
- Move Kubernetes config to overlays when environments diverge.
- Add smoke tests after deployment.
- Add image vulnerability scanning.
