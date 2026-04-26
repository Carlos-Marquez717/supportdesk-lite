package com.portfolio.supportdesklite.ticket.repository;

import com.portfolio.supportdesklite.ticket.dto.TicketFilterRequest;
import com.portfolio.supportdesklite.ticket.model.Ticket;
import org.springframework.data.jpa.domain.Specification;

public final class TicketSpecifications {
    private TicketSpecifications() {
    }

    public static Specification<Ticket> withFilters(TicketFilterRequest filter) {
        return Specification.where(notDeleted())
                .and(filter.status() == null ? null : (root, query, cb) -> cb.equal(root.get("status"), filter.status()))
                .and(filter.priority() == null ? null : (root, query, cb) -> cb.equal(root.get("priority"), filter.priority()))
                .and(filter.category() == null ? null : (root, query, cb) -> cb.equal(root.get("category"), filter.category()))
                .and(filter.agentId() == null ? null : (root, query, cb) -> cb.equal(root.get("assignedAgent").get("id"), filter.agentId()))
                .and(filter.slaBreached() == null ? null : (root, query, cb) -> cb.equal(root.get("slaBreached"), filter.slaBreached()));
    }

    private static Specification<Ticket> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }
}
