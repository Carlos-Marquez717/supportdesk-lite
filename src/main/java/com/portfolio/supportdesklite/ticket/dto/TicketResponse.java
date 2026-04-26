package com.portfolio.supportdesklite.ticket.dto;

import com.portfolio.supportdesklite.ticket.model.TicketCategory;
import com.portfolio.supportdesklite.ticket.model.TicketPriority;
import com.portfolio.supportdesklite.ticket.model.TicketStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        String title,
        TicketStatus status,
        TicketPriority priority,
        TicketCategory category,
        String requesterName,
        String requesterEmail,
        UUID assignedAgentId,
        String assignedAgentName,
        LocalDateTime dueAt,
        boolean slaBreached,
        LocalDateTime resolvedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
