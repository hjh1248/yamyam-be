package com.ssafy.yamyam_coach.domain.postlike;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostLike {

    private Long id;
    private Long postId;
    private Long userId;
    private LocalDateTime createdAt;

    @Builder
    private PostLike(Long postId, Long userId, LocalDateTime createdAt) {
        this.postId = postId;
        this.userId = userId;
        this.createdAt = createdAt;
    }
}
