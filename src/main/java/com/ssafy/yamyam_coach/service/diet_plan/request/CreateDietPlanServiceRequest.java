package com.ssafy.yamyam_coach.service.diet_plan.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateDietPlanServiceRequest {

    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    private CreateDietPlanServiceRequest(String title, String content, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
