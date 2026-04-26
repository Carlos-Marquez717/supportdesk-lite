package com.portfolio.supportdesklite.agent.controller;

import com.portfolio.supportdesklite.agent.dto.AgentResponse;
import com.portfolio.supportdesklite.agent.dto.CreateAgentRequest;
import com.portfolio.supportdesklite.agent.service.AgentService;
import com.portfolio.supportdesklite.common.web.PageResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/agents")
public class AgentController {
    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgentResponse create(@Valid @RequestBody CreateAgentRequest request) {
        return agentService.create(request);
    }

    @GetMapping
    public PageResponse<AgentResponse> findAll(@RequestParam(required = false) Boolean active, @PageableDefault(size = 20) Pageable pageable) {
        return agentService.findAll(active, pageable);
    }

    @GetMapping("/{agentId}")
    public AgentResponse findById(@PathVariable UUID agentId) {
        return agentService.findById(agentId);
    }

    @PatchMapping("/{agentId}/activate")
    public AgentResponse activate(@PathVariable UUID agentId) {
        return agentService.activate(agentId);
    }

    @PatchMapping("/{agentId}/deactivate")
    public AgentResponse deactivate(@PathVariable UUID agentId) {
        return agentService.deactivate(agentId);
    }
}
