package com.aiplatform.customersupport.ticket.repository;

import com.aiplatform.customersupport.ticket.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
