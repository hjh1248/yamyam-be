package com.ssafy.yamyam_coach.repository.daily_diet.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyDietUpdateRequest {

    private Long dailyDietId;
    private LocalDate date;
    private String description;

    @Builder
    private DailyDietUpdateRequest(Long dailyDietId, LocalDate date, String description) {
        this.dailyDietId = dailyDietId;
        this.date = date;
        this.description = description;
    }
}
