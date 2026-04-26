package com.portfolio.supportdesklite.ticket.service;

import com.portfolio.supportdesklite.ticket.model.TicketPriority;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class SlaService {
    private final Clock clock;

    public SlaService(Clock clock) {
        this.clock = clock;
    }

    public LocalDateTime calculateDueAt(TicketPriority priority) {
        LocalDateTime now = LocalDateTime.now(clock);
        return switch (priority) {
            case LOW -> now.plusHours(72);
            case MEDIUM -> now.plusHours(48);
            case HIGH -> now.plusHours(24);
            case URGENT -> now.plusHours(4);
        };
    }

    public TicketPriority escalate(TicketPriority priority) {
        return switch (priority) {
            case LOW -> TicketPriority.MEDIUM;
            case MEDIUM -> TicketPriority.HIGH;
            case HIGH, URGENT -> TicketPriority.URGENT;
        };
    }
}
