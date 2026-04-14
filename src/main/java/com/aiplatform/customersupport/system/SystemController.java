package com.aiplatform.customersupport.system;

import java.time.OffsetDateTime;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of(
                "service", "ai-customer-support",
                "status", "UP",
                "timestamp", OffsetDateTime.now());
    }
}
