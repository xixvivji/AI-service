package com.aiplatform.customersupport.ai.dto;

public record AiAnalysisRequest(
        String subject,
        String content,
        String priority) {
}
