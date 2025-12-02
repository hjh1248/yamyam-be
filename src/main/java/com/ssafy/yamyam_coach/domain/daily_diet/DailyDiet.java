package com.ssafy.yamyam_coach.domain.daily_diet;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyDiet {

    private Long id;
    private Long dietPlanId;
    private LocalDateTime date;
    private String description;

    @Builder
    private DailyDiet(Long dietPlanId, LocalDateTime date, String description) {
        this.dietPlanId = dietPlanId;
        this.date = date;
        this.description = description;
    }
}
