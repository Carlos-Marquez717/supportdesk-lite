package com.portfolio.supportdesklite.ticket.dto;

import com.portfolio.supportdesklite.ticket.model.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeTicketStatusRequest(@NotNull TicketStatus status) {
}
