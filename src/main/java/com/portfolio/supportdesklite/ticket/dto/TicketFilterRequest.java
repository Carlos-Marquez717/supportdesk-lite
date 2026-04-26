package com.portfolio.supportdesklite.ticket.dto;

import com.portfolio.supportdesklite.ticket.model.TicketCategory;
import com.portfolio.supportdesklite.ticket.model.TicketPriority;
import com.portfolio.supportdesklite.ticket.model.TicketStatus;
import java.util.UUID;

public record TicketFilterRequest(
        TicketStatus status,
        TicketPriority priority,
        TicketCategory category,
        UUID agentId,
        Boolean slaBreached
) {
}
