package com.portfolio.supportdesklite.comment.service;

import com.portfolio.supportdesklite.audit.service.AuditEventService;
import com.portfolio.supportdesklite.comment.dto.AddCommentRequest;
import com.portfolio.supportdesklite.comment.dto.CommentResponse;
import com.portfolio.supportdesklite.comment.mapper.CommentMapper;
import com.portfolio.supportdesklite.comment.model.TicketComment;
import com.portfolio.supportdesklite.comment.repository.TicketCommentRepository;
import com.portfolio.supportdesklite.ticket.model.Ticket;
import com.portfolio.supportdesklite.ticket.service.TicketService;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketCommentService {
    private final TicketCommentRepository ticketCommentRepository;
    private final TicketService ticketService;
    private final CommentMapper commentMapper;
    private final AuditEventService auditEventService;

    public TicketCommentService(
            TicketCommentRepository ticketCommentRepository,
            TicketService ticketService,
            CommentMapper commentMapper,
            AuditEventService auditEventService
    ) {
        this.ticketCommentRepository = ticketCommentRepository;
        this.ticketService = ticketService;
        this.commentMapper = commentMapper;
        this.auditEventService = auditEventService;
    }

    @Transactional
    public CommentResponse addComment(UUID ticketId, AddCommentRequest request) {
        Ticket ticket = ticketService.getActiveTicket(ticketId);
        TicketComment comment = ticketCommentRepository.save(commentMapper.toEntity(request, ticket));
        auditEventService.record("TICKET", ticketId, "COMMENT_ADDED", request.authorEmail(), "{\"visibility\":\"" + request.visibility() + "\"}");
        return commentMapper.toResponse(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findByTicket(UUID ticketId) {
        ticketService.getActiveTicket(ticketId);
        return ticketCommentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }
}
