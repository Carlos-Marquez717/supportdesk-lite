package com.portfolio.supportdesklite.dashboard.dto;

import com.portfolio.supportdesklite.ticket.model.TicketPriority;
import com.portfolio.supportdesklite.ticket.model.TicketStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record RecentTicketResponse(
        UUID id,
        String title,
        TicketStatus status,
        TicketPriority priority,
        LocalDateTime createdAt
) {
}
