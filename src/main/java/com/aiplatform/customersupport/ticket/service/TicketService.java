package com.aiplatform.customersupport.ticket.service;

import com.aiplatform.customersupport.common.exception.TicketNotFoundException;
import com.aiplatform.customersupport.ai.dto.AiAnalysisResponse;
import com.aiplatform.customersupport.ai.service.AiAnalysisService;
import com.aiplatform.customersupport.ticket.domain.Ticket;
import com.aiplatform.customersupport.ticket.domain.TicketStatus;
import com.aiplatform.customersupport.ticket.dto.CreateTicketRequest;
import com.aiplatform.customersupport.ticket.dto.TicketResponse;
import com.aiplatform.customersupport.ticket.repository.TicketRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

    private final AiAnalysisService aiAnalysisService;
    private final TicketRepository ticketRepository;

    @Transactional
    public TicketResponse create(CreateTicketRequest request) {
        AiAnalysisResponse analysis = aiAnalysisService.analyze(
                request.subject(),
                request.content(),
                request.priority());

        Ticket savedTicket = ticketRepository.save(Ticket.builder()
                .customerEmail(request.customerEmail())
                .subject(request.subject())
                .content(request.content())
                .priority(request.priority())
                .status(TicketStatus.OPEN)
                .aiCategory(analysis.category())
                .aiSummary(analysis.summary())
                .aiDraftReply(analysis.draftReply())
                .createdAt(OffsetDateTime.now())
                .build());

        return TicketResponse.from(savedTicket);
    }

    public List<TicketResponse> findAll() {
        return ticketRepository.findAll().stream()
                .map(TicketResponse::from)
                .toList();
    }

    public TicketResponse findById(Long id) {
        return ticketRepository.findById(id)
                .map(TicketResponse::from)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }
}
