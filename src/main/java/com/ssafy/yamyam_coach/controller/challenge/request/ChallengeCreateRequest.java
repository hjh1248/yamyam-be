package com.ssafy.yamyam_coach.controller.challenge.request;

import com.ssafy.yamyam_coach.domain.challenge.Challenge;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChallengeCreateRequest {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    // 엔티티 변환 시 생성자 ID(userId)를 받아서 넣음
    public Challenge toEntity(Long userId) {
        return Challenge.builder()
                .creatorId(userId) // ★ 핵심: 생성자 ID 주입
                .title(title)
                .description(description)
                .startDate(startDate.atStartOfDay())
                .endDate(endDate.atTime(23, 59, 59))
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
    }
}