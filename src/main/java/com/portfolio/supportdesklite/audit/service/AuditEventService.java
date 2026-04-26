package com.portfolio.supportdesklite.audit.service;

import com.portfolio.supportdesklite.audit.dto.AuditEventResponse;
import com.portfolio.supportdesklite.audit.model.AuditEvent;
import com.portfolio.supportdesklite.audit.repository.AuditEventRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditEventService {
    private final AuditEventRepository auditEventRepository;

    public AuditEventService(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void record(String entityType, UUID entityId, String action, String actor, String metadataJson) {
        AuditEvent event = new AuditEvent();
        event.setEntityType(entityType);
        event.setEntityId(entityId);
        event.setAction(action);
        event.setActor(actor == null || actor.isBlank() ? "system" : actor);
        event.setMetadataJson(metadataJson);
        auditEventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public List<AuditEventResponse> findTicketEvents(UUID ticketId) {
        return auditEventRepository.findByEntityTypeAndEntityIdOrderByCreatedAtDesc("TICKET", ticketId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AuditEventResponse toResponse(AuditEvent event) {
        return new AuditEventResponse(
                event.getId(),
                event.getEntityType(),
                event.getEntityId(),
                event.getAction(),
                event.getActor(),
                event.getMetadataJson(),
                event.getCreatedAt()
        );
    }
}
