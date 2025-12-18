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

    private int participants;    // 참여자 수
    private int progress;        // 날짜 기준 진행률

    public void calculateProgress() {
        if (startDate == null || endDate == null) return;
        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();
        LocalDate now = LocalDate.now();

        if (now.isBefore(start)) progress = 0;
        else if (now.isAfter(end)) progress = 100;
        else {
            long total = ChronoUnit.DAYS.between(start, end);
            long elapsed = ChronoUnit.DAYS.between(start, now);
            progress = total > 0 ? (int) ((double) elapsed / total * 100) : 100;
        }
    }
}