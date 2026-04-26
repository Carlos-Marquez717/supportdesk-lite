package com.portfolio.supportdesklite.agent.service;

import com.portfolio.supportdesklite.agent.dto.AgentResponse;
import com.portfolio.supportdesklite.agent.dto.CreateAgentRequest;
import com.portfolio.supportdesklite.agent.mapper.AgentMapper;
import com.portfolio.supportdesklite.agent.model.Agent;
import com.portfolio.supportdesklite.agent.repository.AgentRepository;
import com.portfolio.supportdesklite.audit.service.AuditEventService;
import com.portfolio.supportdesklite.common.error.BusinessException;
import com.portfolio.supportdesklite.common.error.ErrorCode;
import com.portfolio.supportdesklite.common.error.NotFoundException;
import com.portfolio.supportdesklite.common.web.PageResponse;
import com.portfolio.supportdesklite.ticket.repository.TicketRepository;
import com.portfolio.supportdesklite.ticket.service.TicketService;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentService {
    private final AgentRepository agentRepository;
    private final TicketRepository ticketRepository;
    private final AgentMapper agentMapper;
    private final AuditEventService auditEventService;

    public AgentService(AgentRepository agentRepository, TicketRepository ticketRepository, AgentMapper agentMapper, AuditEventService auditEventService) {
        this.agentRepository = agentRepository;
        this.ticketRepository = ticketRepository;
        this.agentMapper = agentMapper;
        this.auditEventService = auditEventService;
    }

    @Transactional
    public AgentResponse create(CreateAgentRequest request) {
        if (agentRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessException(ErrorCode.DUPLICATE_AGENT_EMAIL, HttpStatus.CONFLICT, "Agent email already exists");
        }
        Agent agent = agentRepository.save(agentMapper.toEntity(request));
        auditEventService.record("AGENT", agent.getId(), "AGENT_CREATED", request.email(), null);
        return toResponse(agent);
    }

    @Transactional(readOnly = true)
    public PageResponse<AgentResponse> findAll(Boolean active, Pageable pageable) {
        Page<Agent> page = active == null ? agentRepository.findAll(pageable) : agentRepository.findByActive(active, pageable);
        return PageResponse.from(page.map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public AgentResponse findById(UUID id) {
        return toResponse(getAgent(id));
    }

    @Transactional
    public AgentResponse activate(UUID id) {
        Agent agent = getAgent(id);
        agent.setActive(true);
        auditEventService.record("AGENT", agent.getId(), "AGENT_ACTIVATED", "system", null);
        return toResponse(agentRepository.save(agent));
    }

    @Transactional
    public AgentResponse deactivate(UUID id) {
        Agent agent = getAgent(id);
        long openTickets = assignedOpenTickets(agent);
        if (openTickets > 0) {
            throw new BusinessException(ErrorCode.AGENT_HAS_OPEN_TICKETS, HttpStatus.CONFLICT, "Agent has open assigned tickets");
        }
        agent.setActive(false);
        auditEventService.record("AGENT", agent.getId(), "AGENT_DEACTIVATED", "system", null);
        return toResponse(agentRepository.save(agent));
    }

    private Agent getAgent(UUID id) {
        return agentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.AGENT_NOT_FOUND, "Agent not found"));
    }

    private AgentResponse toResponse(Agent agent) {
        return agentMapper.toResponse(agent, assignedOpenTickets(agent));
    }

    private long assignedOpenTickets(Agent agent) {
        return ticketRepository.countByAssignedAgentAndStatusInAndDeletedAtIsNull(agent, TicketService.openStatuses());
    }
}
