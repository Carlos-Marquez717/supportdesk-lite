package com.portfolio.supportdesklite.ticket.dto;

import com.portfolio.supportdesklite.ticket.model.TicketCategory;
import com.portfolio.supportdesklite.ticket.model.TicketPriority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
        @NotBlank @Size(min = 5, max = 160) String title,
        @NotBlank @Size(min = 10, max = 3000) String description,
        @NotNull TicketPriority priority,
        @NotNull TicketCategory category,
        @NotBlank @Size(max = 120) String requesterName,
        @NotBlank @Email @Size(max = 160) String requesterEmail
) {
}
