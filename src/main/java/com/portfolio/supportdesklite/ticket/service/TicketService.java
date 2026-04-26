package com.portfolio.supportdesklite.ticket.service;

import com.portfolio.supportdesklite.agent.model.Agent;
import com.portfolio.supportdesklite.agent.repository.AgentRepository;
import com.portfolio.supportdesklite.audit.service.AuditEventService;
import com.portfolio.supportdesklite.common.error.BusinessException;
import com.portfolio.supportdesklite.common.error.ErrorCode;
import com.portfolio.supportdesklite.common.error.NotFoundException;
import com.portfolio.supportdesklite.common.web.PageResponse;
import com.portfolio.supportdesklite.ticket.dto.AssignTicketRequest;
import com.portfolio.supportdesklite.ticket.dto.ChangeTicketStatusRequest;
import com.portfolio.supportdesklite.ticket.dto.CreateTicketRequest;
import com.portfolio.supportdesklite.ticket.dto.ResolveTicketRequest;
import com.portfolio.supportdesklite.ticket.dto.TicketDetailResponse;
import com.portfolio.supportdesklite.ticket.dto.TicketFilterRequest;
import com.portfolio.supportdesklite.ticket.dto.TicketResponse;
import com.portfolio.supportdesklite.ticket.dto.UpdateTicketRequest;
import com.portfolio.supportdesklite.ticket.mapper.TicketMapper;
import com.portfolio.supportdesklite.ticket.model.Ticket;
import com.portfolio.supportdesklite.ticket.model.TicketStatus;
import com.portfolio.supportdesklite.ticket.repository.TicketRepository;
import com.portfolio.supportdesklite.ticket.repository.TicketSpecifications;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {
    private static final Set<TicketStatus> OPEN_STATUSES = Set.of(TicketStatus.OPEN, TicketStatus.IN_PROGRESS, TicketStatus.WAITING_CUSTOMER);
    private static final Map<TicketStatus, Set<TicketStatus>> ALLOWED_TRANSITIONS = Map.of(
            TicketStatus.OPEN, Set.of(TicketStatus.IN_PROGRESS, TicketStatus.WAITING_CUSTOMER, TicketStatus.RESOLVED),
            TicketStatus.IN_PROGRESS, Set.of(TicketStatus.WAITING_CUSTOMER, TicketStatus.RESOLVED),
            TicketStatus.WAITING_CUSTOMER, Set.of(TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED),
            TicketStatus.RESOLVED, Set.of(TicketStatus.CLOSED, TicketStatus.OPEN),
            TicketStatus.CLOSED, Set.of(TicketStatus.OPEN)
    );

    private final TicketRepository ticketRepository;
    private final AgentRepository agentRepository;
    private final TicketMapper ticketMapper;
    private final SlaService slaService;
    private final AuditEventService auditEventService;
    private final Clock clock;

    public TicketService(
            TicketRepository ticketRepository,
            AgentRepository agentRepository,
            TicketMapper ticketMapper,
            SlaService slaService,
            AuditEventService auditEventService,
            Clock clock
    ) {
        this.ticketRepository = ticketRepository;
        this.agentRepository = agentRepository;
        this.ticketMapper = ticketMapper;
        this.slaService = slaService;
        this.auditEventService = auditEventService;
        this.clock = clock;
    }

    @Transactional
    public TicketResponse create(CreateTicketRequest request) {
        Ticket ticket = ticketMapper.toEntity(request);
        ticket.setDueAt(slaService.calculateDueAt(request.priority()));
        Ticket saved = ticketRepository.save(ticket);
        auditEventService.record("TICKET", saved.getId(), "TICKET_CREATED", request.requesterEmail(), "{\"priority\":\"" + request.priority() + "\"}");
        return ticketMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<TicketResponse> findAll(TicketFilterRequest filter, Pageable pageable) {
        return PageResponse.from(ticketRepository.findAll(TicketSpecifications.withFilters(filter), pageable).map(ticketMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public TicketDetailResponse findById(UUID id) {
        return ticketMapper.toDetailResponse(getActiveTicket(id));
    }

    @Transactional
    public TicketResponse update(UUID id, UpdateTicketRequest request) {
        Ticket ticket = getActiveTicket(id);
        if (ticket.isClosedState()) {
            throw conflict(ErrorCode.INVALID_TICKET_OPERATION, "Closed tickets cannot be edited");
        }
        boolean priorityChanged = ticket.getPriority() != request.priority();
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setPriority(request.priority());
        ticket.setCategory(request.category());
        ticket.setRequesterName(request.requesterName());
        ticket.setRequesterEmail(request.requesterEmail().toLowerCase());
        if (priorityChanged && ticket.isOpenWorkState()) {
            ticket.setDueAt(slaService.calculateDueAt(request.priority()));
            ticket.setSlaBreached(false);
        }
        auditEventService.record("TICKET", ticket.getId(), "TICKET_UPDATED", request.requesterEmail(), null);
        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketResponse changeStatus(UUID id, ChangeTicketStatusRequest request) {
        Ticket ticket = getActiveTicket(id);
        TicketStatus target = request.status();
        if (!ALLOWED_TRANSITIONS.getOrDefault(ticket.getStatus(), Set.of()).contains(target)) {
            throw conflict(ErrorCode.INVALID_STATUS_TRANSITION, "Transition from " + ticket.getStatus() + " to " + target + " is not allowed");
        }
        if (target == TicketStatus.RESOLVED) {
            throw conflict(ErrorCode.INVALID_TICKET_OPERATION, "Use the resolve endpoint to resolve a ticket with a resolution summary");
        }
        if (target == TicketStatus.OPEN) {
            ticket.setResolvedAt(null);
            ticket.setResolutionSummary(null);
            ticket.setDueAt(slaService.calculateDueAt(ticket.getPriority()));
            ticket.setSlaBreached(false);
        }
        TicketStatus previous = ticket.getStatus();
        ticket.setStatus(target);
        auditEventService.record("TICKET", ticket.getId(), "TICKET_STATUS_CHANGED", "system", "{\"from\":\"" + previous + "\",\"to\":\"" + target + "\"}");
        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketResponse assign(UUID id, AssignTicketRequest request) {
        Ticket ticket = getActiveTicket(id);
        if (ticket.isClosedState()) {
            throw conflict(ErrorCode.INVALID_TICKET_OPERATION, "Closed tickets cannot be assigned");
        }
        Agent agent = agentRepository.findById(request.agentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.AGENT_NOT_FOUND, "Agent not found"));
        if (!agent.isActive()) {
            throw conflict(ErrorCode.INVALID_TICKET_OPERATION, "Inactive agents cannot receive tickets");
        }
        ticket.setAssignedAgent(agent);
        auditEventService.record("TICKET", ticket.getId(), "TICKET_ASSIGNED", "system", "{\"agentId\":\"" + agent.getId() + "\"}");
        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketResponse resolve(UUID id, ResolveTicketRequest request) {
        Ticket ticket = getActiveTicket(id);
        if (!ticket.isOpenWorkState()) {
            throw conflict(ErrorCode.INVALID_TICKET_OPERATION, "Only open tickets can be resolved");
        }
        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setResolvedAt(LocalDateTime.now(clock));
        ticket.setResolutionSummary(request.resolutionSummary());
        auditEventService.record("TICKET", ticket.getId(), "TICKET_RESOLVED", "system", null);
        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketResponse reopen(UUID id) {
        Ticket ticket = getActiveTicket(id);
        if (ticket.getStatus() != TicketStatus.RESOLVED && ticket.getStatus() != TicketStatus.CLOSED) {
            throw conflict(ErrorCode.INVALID_TICKET_OPERATION, "Only resolved or closed tickets can be reopened");
        }
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setResolvedAt(null);
        ticket.setResolutionSummary(null);
        ticket.setDueAt(slaService.calculateDueAt(ticket.getPriority()));
        ticket.setSlaBreached(false);
        auditEventService.record("TICKET", ticket.getId(), "TICKET_REOPENED", "system", null);
        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    @Transactional
    public void delete(UUID id) {
        Ticket ticket = getActiveTicket(id);
        ticket.setDeletedAt(LocalDateTime.now(clock));
        auditEventService.record("TICKET", ticket.getId(), "TICKET_DELETED", "system", null);
        ticketRepository.save(ticket);
    }

    public Ticket getActiveTicket(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TICKET_NOT_FOUND, "Ticket not found"));
        if (ticket.isDeleted()) {
            throw new NotFoundException(ErrorCode.TICKET_NOT_FOUND, "Ticket not found");
        }
        return ticket;
    }

    public static Set<TicketStatus> openStatuses() {
        return OPEN_STATUSES;
    }

    private BusinessException conflict(ErrorCode errorCode, String message) {
        return new BusinessException(errorCode, HttpStatus.CONFLICT, message);
    }
}
