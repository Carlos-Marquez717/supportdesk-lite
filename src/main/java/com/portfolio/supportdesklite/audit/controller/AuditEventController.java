package com.portfolio.supportdesklite.audit.controller;

import com.portfolio.supportdesklite.audit.dto.AuditEventResponse;
import com.portfolio.supportdesklite.audit.service.AuditEventService;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets/{ticketId}/audit-events")
public class AuditEventController {
    private final AuditEventService auditEventService;

    public AuditEventController(AuditEventService auditEventService) {
        this.auditEventService = auditEventService;
    }

    @GetMapping
    public List<AuditEventResponse> findTicketEvents(@PathVariable UUID ticketId) {
        return auditEventService.findTicketEvents(ticketId);
    }
}
