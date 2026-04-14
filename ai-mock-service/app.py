from fastapi import FastAPI
from pydantic import BaseModel


class AiAnalysisRequest(BaseModel):
    subject: str
    content: str
    priority: str


class AiAnalysisResponse(BaseModel):
    category: str
    summary: str
    draftReply: str


app = FastAPI(title="ai-mock-service", version="0.1.0")


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "UP"}


@app.post("/api/v1/analyze", response_model=AiAnalysisResponse)
def analyze(payload: AiAnalysisRequest) -> AiAnalysisResponse:
    content = payload.content.lower()

    if "refund" in content or "cancel" in content or "payment" in content:
        category = "BILLING"
    elif "login" in content or "password" in content or "account" in content:
        category = "ACCOUNT"
    elif "error" in content or "bug" in content or "fail" in content:
        category = "TECHNICAL"
    elif "delivery" in content or "shipping" in content or "late" in content:
        category = "DELIVERY"
    else:
        category = "GENERAL"

    preview = payload.content[:160]

    return AiAnalysisResponse(
        category=category,
        summary=f"[{payload.priority}] {preview}",
        draftReply=(
            f"Your request about '{payload.subject}' has been routed to the {category.lower()} queue. "
            "An agent will review the details and follow up shortly."
        ),
    )
