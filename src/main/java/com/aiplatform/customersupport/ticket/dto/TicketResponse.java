package com.aiplatform.customersupport.ticket.dto;

import com.aiplatform.customersupport.ticket.domain.Ticket;
import com.aiplatform.customersupport.ticket.domain.TicketPriority;
import com.aiplatform.customersupport.ticket.domain.TicketStatus;
import java.time.OffsetDateTime;

public record TicketResponse(
        Long id,
        String customerEmail,
        String subject,
        String content,
        TicketPriority priority,
        TicketStatus status,
        String aiCategory,
        String aiSummary,
        String aiDraftReply,
        OffsetDateTime createdAt) {

    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getCustomerEmail(),
                ticket.getSubject(),
                ticket.getContent(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getAiCategory(),
                ticket.getAiSummary(),
                ticket.getAiDraftReply(),
                ticket.getCreatedAt());
    }
}
