package com.portfolio.supportdesklite.comment.dto;

import com.portfolio.supportdesklite.comment.model.CommentVisibility;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddCommentRequest(
        @NotBlank @Size(max = 120) String authorName,
        @NotBlank @Email @Size(max = 160) String authorEmail,
        @NotNull CommentVisibility visibility,
        @NotBlank @Size(min = 2, max = 2000) String content
) {
}
