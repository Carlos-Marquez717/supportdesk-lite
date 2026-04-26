package com.portfolio.supportdesklite.ticket.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.portfolio.supportdesklite.ticket.model.TicketPriority;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class SlaServiceTest {

    private final Clock fixedClock = Clock.fixed(Instant.parse("2026-04-26T10:00:00Z"), ZoneOffset.UTC);
    private final SlaService slaService = new SlaService(fixedClock);

    @Test
    void calculatesDueAtByPriority() {
        LocalDateTime now = LocalDateTime.of(2026, 4, 26, 10, 0);

        assertThat(slaService.calculateDueAt(TicketPriority.LOW)).isEqualTo(now.plusHours(72));
        assertThat(slaService.calculateDueAt(TicketPriority.MEDIUM)).isEqualTo(now.plusHours(48));
        assertThat(slaService.calculateDueAt(TicketPriority.HIGH)).isEqualTo(now.plusHours(24));
        assertThat(slaService.calculateDueAt(TicketPriority.URGENT)).isEqualTo(now.plusHours(4));
    }

    @Test
    void escalatesPriorityUntilUrgent() {
        assertThat(slaService.escalate(TicketPriority.LOW)).isEqualTo(TicketPriority.MEDIUM);
        assertThat(slaService.escalate(TicketPriority.MEDIUM)).isEqualTo(TicketPriority.HIGH);
        assertThat(slaService.escalate(TicketPriority.HIGH)).isEqualTo(TicketPriority.URGENT);
        assertThat(slaService.escalate(TicketPriority.URGENT)).isEqualTo(TicketPriority.URGENT);
    }
}
