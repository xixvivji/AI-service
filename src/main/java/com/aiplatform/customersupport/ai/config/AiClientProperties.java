package com.aiplatform.customersupport.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai.client")
public record AiClientProperties(
        String baseUrl,
        String analysisPath,
        int connectTimeoutMillis,
        int readTimeoutMillis) {
}
