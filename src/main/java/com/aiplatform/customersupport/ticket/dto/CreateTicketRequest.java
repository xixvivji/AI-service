package com.aiplatform.customersupport.ticket.dto;

import com.aiplatform.customersupport.ticket.domain.TicketPriority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
        @Email @NotBlank String customerEmail,
        @NotBlank @Size(max = 180) String subject,
        @NotBlank @Size(max = 5000) String content,
        @NotNull TicketPriority priority) {
}
