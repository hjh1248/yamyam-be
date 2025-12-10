package com.ssafy.yamyam_coach.service.daily_diet.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyDietUpdateDescriptionServiceRequest {

    private Long dailyDietId;
    private LocalDate date;
    private String newDescription;
}
