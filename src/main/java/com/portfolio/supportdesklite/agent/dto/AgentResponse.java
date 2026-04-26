package com.portfolio.supportdesklite.agent.dto;

import com.portfolio.supportdesklite.agent.model.AgentRole;
import java.time.LocalDateTime;
import java.util.UUID;

public record AgentResponse(
        UUID id,
        String name,
        String email,
        AgentRole role,
        boolean active,
        long assignedOpenTickets,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
