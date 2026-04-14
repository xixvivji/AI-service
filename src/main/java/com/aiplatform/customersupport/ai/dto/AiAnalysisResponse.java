package com.aiplatform.customersupport.ai.dto;

public record AiAnalysisResponse(
        String category,
        String summary,
        String draftReply) {
}
