package com.portfolio.supportdesklite.ticket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResolveTicketRequest(@NotBlank @Size(min = 10, max = 2000) String resolutionSummary) {
}
