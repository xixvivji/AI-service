package com.aiplatform.customersupport.ai.service;

import com.aiplatform.customersupport.ai.config.AiClientProperties;
import com.aiplatform.customersupport.ai.dto.AiAnalysisRequest;
import com.aiplatform.customersupport.ai.dto.AiAnalysisResponse;
import com.aiplatform.customersupport.ticket.domain.TicketPriority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class AiAnalysisService {

    private final RestClient aiRestClient;
    private final AiClientProperties properties;

    public AiAnalysisResponse analyze(String subject, String content, TicketPriority priority) {
        try {
            AiAnalysisResponse response = aiRestClient.post()
                    .uri(properties.analysisPath())
                    .body(new AiAnalysisRequest(subject, content, priority.name()))
                    .retrieve()
                    .body(AiAnalysisResponse.class);

            if (response != null) {
                return response;
            }
        } catch (Exception ignored) {
            // Fall back to deterministic local placeholders while the AI service is unavailable.
        }

        return fallback(subject, content);
    }

    private AiAnalysisResponse fallback(String subject, String content) {
        String normalizedContent = content.toLowerCase();

        String category = "GENERAL";
        if (normalizedContent.contains("refund") || normalizedContent.contains("cancel")) {
            category = "BILLING";
        } else if (normalizedContent.contains("login") || normalizedContent.contains("password")) {
            category = "ACCOUNT";
        } else if (normalizedContent.contains("error") || normalizedContent.contains("bug")) {
            category = "TECHNICAL";
        }

        int previewLength = Math.min(content.length(), 120);

        return new AiAnalysisResponse(
                category,
                "Customer issue summary: " + content.substring(0, previewLength),
                "We have received your request about \"" + subject
                        + "\" and our support team is reviewing it. "
                        + "A detailed response will follow after validation.");
    }
}
