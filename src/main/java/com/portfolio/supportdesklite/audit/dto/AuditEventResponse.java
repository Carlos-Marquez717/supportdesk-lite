package com.portfolio.supportdesklite.audit.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditEventResponse(
        UUID id,
        String entityType,
        UUID entityId,
        String action,
        String actor,
        String metadataJson,
        LocalDateTime createdAt
) {
}
