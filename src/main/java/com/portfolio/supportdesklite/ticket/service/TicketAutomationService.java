package com.portfolio.supportdesklite.ticket.service;

import com.portfolio.supportdesklite.audit.service.AuditEventService;
import com.portfolio.supportdesklite.ticket.model.Ticket;
import com.portfolio.supportdesklite.ticket.model.TicketPriority;
import com.portfolio.supportdesklite.ticket.repository.TicketRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketAutomationService {
    private final TicketRepository ticketRepository;
    private final SlaService slaService;
    private final AuditEventService auditEventService;
    private final Clock clock;

    public TicketAutomationService(TicketRepository ticketRepository, SlaService slaService, AuditEventService auditEventService, Clock clock) {
        this.ticketRepository = ticketRepository;
        this.slaService = slaService;
        this.auditEventService = auditEventService;
        this.clock = clock;
    }

    @Scheduled(fixedDelayString = "${supportdesk.automation.sla-check-delay-ms:300000}")
    @Transactional
    public void markBreachedTickets() {
        List<Ticket> breached = ticketRepository.findByDeletedAtIsNullAndSlaBreachedFalseAndStatusInAndDueAtBefore(
                TicketService.openStatuses(),
                LocalDateTime.now(clock)
        );

        for (Ticket ticket : breached) {
            TicketPriority previousPriority = ticket.getPriority();
            TicketPriority escalatedPriority = slaService.escalate(previousPriority);
            ticket.setSlaBreached(true);
            ticket.setPriority(escalatedPriority);
            auditEventService.record("TICKET", ticket.getId(), "SLA_BREACHED", "system", null);
            if (previousPriority != escalatedPriority) {
                auditEventService.record("TICKET", ticket.getId(), "PRIORITY_ESCALATED", "system",
                        "{\"from\":\"" + previousPriority + "\",\"to\":\"" + escalatedPriority + "\"}");
            }
        }
    }
}
