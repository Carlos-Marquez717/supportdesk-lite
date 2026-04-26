package com.portfolio.supportdesklite.comment.mapper;

import com.portfolio.supportdesklite.comment.dto.AddCommentRequest;
import com.portfolio.supportdesklite.comment.dto.CommentResponse;
import com.portfolio.supportdesklite.comment.model.TicketComment;
import com.portfolio.supportdesklite.ticket.model.Ticket;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public TicketComment toEntity(AddCommentRequest request, Ticket ticket) {
        TicketComment comment = new TicketComment();
        comment.setTicket(ticket);
        comment.setAuthorName(request.authorName());
        comment.setAuthorEmail(request.authorEmail().toLowerCase());
        comment.setVisibility(request.visibility());
        comment.setContent(request.content());
        return comment;
    }

    public CommentResponse toResponse(TicketComment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getTicket().getId(),
                comment.getAuthorName(),
                comment.getAuthorEmail(),
                comment.getVisibility(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
