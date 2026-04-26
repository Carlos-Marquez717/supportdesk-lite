package com.portfolio.supportdesklite.ticket.mapper;

import com.portfolio.supportdesklite.ticket.dto.CreateTicketRequest;
import com.portfolio.supportdesklite.ticket.dto.TicketDetailResponse;
import com.portfolio.supportdesklite.ticket.dto.TicketResponse;
import com.portfolio.supportdesklite.ticket.model.Ticket;
import com.portfolio.supportdesklite.ticket.model.TicketStatus;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public Ticket toEntity(CreateTicketRequest request) {
        Ticket ticket = new Ticket();
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setPriority(request.priority());
        ticket.setCategory(request.category());
        ticket.setRequesterName(request.requesterName());
        ticket.setRequesterEmail(request.requesterEmail().toLowerCase());
        ticket.setStatus(TicketStatus.OPEN);
        return ticket;
    }

    public TicketResponse toResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getCategory(),
                ticket.getRequesterName(),
                ticket.getRequesterEmail(),
                ticket.getAssignedAgent() == null ? null : ticket.getAssignedAgent().getId(),
                ticket.getAssignedAgent() == null ? null : ticket.getAssignedAgent().getName(),
                ticket.getDueAt(),
                ticket.isSlaBreached(),
                ticket.getResolvedAt(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }

    public TicketDetailResponse toDetailResponse(Ticket ticket) {
        return new TicketDetailResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getCategory(),
                ticket.getRequesterName(),
                ticket.getRequesterEmail(),
                ticket.getAssignedAgent() == null ? null : ticket.getAssignedAgent().getId(),
                ticket.getAssignedAgent() == null ? null : ticket.getAssignedAgent().getName(),
                ticket.getDueAt(),
                ticket.isSlaBreached(),
                ticket.getResolvedAt(),
                ticket.getResolutionSummary(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }
}
