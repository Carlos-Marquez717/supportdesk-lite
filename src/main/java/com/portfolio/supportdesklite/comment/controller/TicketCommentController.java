package com.portfolio.supportdesklite.comment.controller;

import com.portfolio.supportdesklite.comment.dto.AddCommentRequest;
import com.portfolio.supportdesklite.comment.dto.CommentResponse;
import com.portfolio.supportdesklite.comment.service.TicketCommentService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets/{ticketId}/comments")
public class TicketCommentController {
    private final TicketCommentService ticketCommentService;

    public TicketCommentController(TicketCommentService ticketCommentService) {
        this.ticketCommentService = ticketCommentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(@PathVariable UUID ticketId, @Valid @RequestBody AddCommentRequest request) {
        return ticketCommentService.addComment(ticketId, request);
    }

    @GetMapping
    public List<CommentResponse> findByTicket(@PathVariable UUID ticketId) {
        return ticketCommentService.findByTicket(ticketId);
    }
}
