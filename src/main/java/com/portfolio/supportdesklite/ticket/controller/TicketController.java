package com.portfolio.supportdesklite.ticket.controller;

import com.portfolio.supportdesklite.common.web.PageResponse;
import com.portfolio.supportdesklite.ticket.dto.AssignTicketRequest;
import com.portfolio.supportdesklite.ticket.dto.ChangeTicketStatusRequest;
import com.portfolio.supportdesklite.ticket.dto.CreateTicketRequest;
import com.portfolio.supportdesklite.ticket.dto.ResolveTicketRequest;
import com.portfolio.supportdesklite.ticket.dto.TicketDetailResponse;
import com.portfolio.supportdesklite.ticket.dto.TicketFilterRequest;
import com.portfolio.supportdesklite.ticket.dto.TicketResponse;
import com.portfolio.supportdesklite.ticket.dto.UpdateTicketRequest;
import com.portfolio.supportdesklite.ticket.model.TicketCategory;
import com.portfolio.supportdesklite.ticket.model.TicketPriority;
import com.portfolio.supportdesklite.ticket.model.TicketStatus;
import com.portfolio.supportdesklite.ticket.service.TicketService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse create(@Valid @RequestBody CreateTicketRequest request) {
        return ticketService.create(request);
    }

    @GetMapping
    public PageResponse<TicketResponse> findAll(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority,
            @RequestParam(required = false) TicketCategory category,
            @RequestParam(required = false) UUID agentId,
            @RequestParam(required = false) Boolean slaBreached,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ticketService.findAll(new TicketFilterRequest(status, priority, category, agentId, slaBreached), pageable);
    }

    @GetMapping("/{ticketId}")
    public TicketDetailResponse findById(@PathVariable UUID ticketId) {
        return ticketService.findById(ticketId);
    }

    @PutMapping("/{ticketId}")
    public TicketResponse update(@PathVariable UUID ticketId, @Valid @RequestBody UpdateTicketRequest request) {
        return ticketService.update(ticketId, request);
    }

    @PatchMapping("/{ticketId}/status")
    public TicketResponse changeStatus(@PathVariable UUID ticketId, @Valid @RequestBody ChangeTicketStatusRequest request) {
        return ticketService.changeStatus(ticketId, request);
    }

    @PatchMapping("/{ticketId}/assignment")
    public TicketResponse assign(@PathVariable UUID ticketId, @Valid @RequestBody AssignTicketRequest request) {
        return ticketService.assign(ticketId, request);
    }

    @PatchMapping("/{ticketId}/resolve")
    public TicketResponse resolve(@PathVariable UUID ticketId, @Valid @RequestBody ResolveTicketRequest request) {
        return ticketService.resolve(ticketId, request);
    }

    @PatchMapping("/{ticketId}/reopen")
    public TicketResponse reopen(@PathVariable UUID ticketId) {
        return ticketService.reopen(ticketId);
    }

    @DeleteMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID ticketId) {
        ticketService.delete(ticketId);
    }
}
