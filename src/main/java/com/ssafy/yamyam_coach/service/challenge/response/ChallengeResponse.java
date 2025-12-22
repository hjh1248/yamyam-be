package com.ssafy.yamyam_coach.service.challenge.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter @Setter
public class ChallengeResponse {
    private Long id;
    private Long creatorId;      // ★ 프론트에서 내 거인지 확인용
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String challengeStatus; // 챌린지 상태 (ACTIVE, DELETED)
    private String myStatus;        // 내 참여 상태 (PROGRESS)

    private int successCount;
    private int participants;    // 참여자 수
    private int progress;        // 날짜 기준 진행률

    public void calculateProgress() {
        if (startDate == null || endDate == null) {
            this.progress = 0;
            return;
        }

        // 전체 기간 (일수)
        long totalDays = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()) + 1;

        // 진행률 계산 (성공횟수 / 전체일수 * 100)
        if (totalDays > 0) {
            this.progress = (int) ((double) this.successCount / totalDays * 100);
            if (this.progress > 100) this.progress = 100;
        } else {
            this.progress = 0;
        }
    }
}