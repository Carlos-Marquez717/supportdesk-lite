package com.portfolio.supportdesklite.dashboard.service;

import com.portfolio.supportdesklite.agent.repository.AgentRepository;
import com.portfolio.supportdesklite.dashboard.dto.DashboardSummaryResponse;
import com.portfolio.supportdesklite.dashboard.dto.GroupedMetricResponse;
import com.portfolio.supportdesklite.dashboard.dto.RecentTicketResponse;
import com.portfolio.supportdesklite.ticket.model.Ticket;
import com.portfolio.supportdesklite.ticket.model.TicketPriority;
import com.portfolio.supportdesklite.ticket.model.TicketStatus;
import com.portfolio.supportdesklite.ticket.repository.TicketRepository;
import com.portfolio.supportdesklite.ticket.repository.TicketSpecifications;
import com.portfolio.supportdesklite.ticket.dto.TicketFilterRequest;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {
    private final TicketRepository ticketRepository;
    private final AgentRepository agentRepository;

    public DashboardService(TicketRepository ticketRepository, AgentRepository agentRepository) {
        this.ticketRepository = ticketRepository;
        this.agentRepository = agentRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse summary() {
        long total = ticketRepository.count(TicketSpecifications.withFilters(new TicketFilterRequest(null, null, null, null, null)));
        return new DashboardSummaryResponse(
                total,
                ticketRepository.countByStatusAndDeletedAtIsNull(TicketStatus.OPEN),
                ticketRepository.countByStatusAndDeletedAtIsNull(TicketStatus.IN_PROGRESS),
                ticketRepository.countByStatusAndDeletedAtIsNull(TicketStatus.RESOLVED),
                ticketRepository.countBySlaBreachedTrueAndDeletedAtIsNull(),
                averageResolutionHours()
        );
    }

    @Transactional(readOnly = true)
    public List<GroupedMetricResponse> ticketsByPriority() {
        return Arrays.stream(TicketPriority.values())
                .map(priority -> new GroupedMetricResponse(priority.name(), ticketRepository.count(TicketSpecifications.withFilters(new TicketFilterRequest(null, priority, null, null, null)))))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GroupedMetricResponse> ticketsByAgent() {
        return agentRepository.findAll().stream()
                .map(agent -> new GroupedMetricResponse(agent.getName(), ticketRepository.count(TicketSpecifications.withFilters(new TicketFilterRequest(null, null, null, agent.getId(), null)))))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RecentTicketResponse> recentTickets(int limit) {
        return ticketRepository.findAll(TicketSpecifications.withFilters(new TicketFilterRequest(null, null, null, null, null)),
                        PageRequest.of(0, Math.max(1, Math.min(limit, 50)), Sort.by(Sort.Direction.DESC, "createdAt")))
                .stream()
                .map(this::toRecent)
                .toList();
    }

    private double averageResolutionHours() {
        List<Ticket> resolvedTickets = ticketRepository.findByResolvedAtIsNotNullAndDeletedAtIsNull();
        return resolvedTickets.stream()
                .mapToLong(ticket -> java.time.Duration.between(ticket.getCreatedAt(), ticket.getResolvedAt()).toHours())
                .average()
                .orElse(0);
    }

    private RecentTicketResponse toRecent(Ticket ticket) {
        return new RecentTicketResponse(ticket.getId(), ticket.getTitle(), ticket.getStatus(), ticket.getPriority(), ticket.getCreatedAt());
    }
}
