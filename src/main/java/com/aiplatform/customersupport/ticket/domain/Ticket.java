package com.aiplatform.customersupport.ticket.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "tickets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String customerEmail;

    @Column(nullable = false, length = 180)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketStatus status;

    @Column(nullable = false, length = 60)
    private String aiCategory;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String aiSummary;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String aiDraftReply;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Builder
    private Ticket(
            String customerEmail,
            String subject,
            String content,
            TicketPriority priority,
            TicketStatus status,
            String aiCategory,
            String aiSummary,
            String aiDraftReply,
            OffsetDateTime createdAt) {
        this.customerEmail = customerEmail;
        this.subject = subject;
        this.content = content;
        this.priority = priority;
        this.status = status;
        this.aiCategory = aiCategory;
        this.aiSummary = aiSummary;
        this.aiDraftReply = aiDraftReply;
        this.createdAt = createdAt;
    }
}
