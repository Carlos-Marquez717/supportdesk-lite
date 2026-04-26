package com.portfolio.supportdesklite.agent.mapper;

import com.portfolio.supportdesklite.agent.dto.AgentResponse;
import com.portfolio.supportdesklite.agent.dto.CreateAgentRequest;
import com.portfolio.supportdesklite.agent.model.Agent;
import org.springframework.stereotype.Component;

@Component
public class AgentMapper {

    public Agent toEntity(CreateAgentRequest request) {
        Agent agent = new Agent();
        agent.setName(request.name());
        agent.setEmail(request.email().toLowerCase());
        agent.setRole(request.role());
        agent.setActive(true);
        return agent;
    }

    public AgentResponse toResponse(Agent agent, long assignedOpenTickets) {
        return new AgentResponse(
                agent.getId(),
                agent.getName(),
                agent.getEmail(),
                agent.getRole(),
                agent.isActive(),
                assignedOpenTickets,
                agent.getCreatedAt(),
                agent.getUpdatedAt()
        );
    }
}
