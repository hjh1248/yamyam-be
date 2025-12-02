package com.ssafy.yamyam_coach.domain.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private Long id;
    private Long dietPlanId;
    private Long userId;

    private String title;
    private String content;
    private int copyCount;
    private int likeCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private Post(Long dietPlanId, Long userId, String title, String content, int copyCount, int likeCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.dietPlanId = dietPlanId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.copyCount = copyCount;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
