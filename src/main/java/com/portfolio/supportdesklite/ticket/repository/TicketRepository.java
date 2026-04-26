package com.portfolio.supportdesklite.ticket.repository;

import com.portfolio.supportdesklite.agent.model.Agent;
import com.portfolio.supportdesklite.ticket.model.Ticket;
import com.portfolio.supportdesklite.ticket.model.TicketStatus;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TicketRepository extends JpaRepository<Ticket, UUID>, JpaSpecificationExecutor<Ticket> {
    long countByAssignedAgentAndStatusInAndDeletedAtIsNull(Agent agent, Collection<TicketStatus> statuses);

    long countByStatusAndDeletedAtIsNull(TicketStatus status);

    long countBySlaBreachedTrueAndDeletedAtIsNull();

    List<Ticket> findTop10ByDeletedAtIsNullOrderByCreatedAtDesc();

    List<Ticket> findByDeletedAtIsNullAndSlaBreachedFalseAndStatusInAndDueAtBefore(
            Collection<TicketStatus> statuses,
            LocalDateTime dueAt
    );

    List<Ticket> findByResolvedAtIsNotNullAndDeletedAtIsNull();
}
