package com.portfolio.supportdesklite.comment.dto;

import com.portfolio.supportdesklite.comment.model.CommentVisibility;
import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        UUID ticketId,
        String authorName,
        String authorEmail,
        CommentVisibility visibility,
        String content,
        LocalDateTime createdAt
) {
}
