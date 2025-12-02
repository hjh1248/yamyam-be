package com.ssafy.yamyam_coach.domain.dietplan;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DietPlan {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private boolean isShared;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private DietPlan(Long userId, String title, String content, boolean isShared, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.isShared = isShared;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
