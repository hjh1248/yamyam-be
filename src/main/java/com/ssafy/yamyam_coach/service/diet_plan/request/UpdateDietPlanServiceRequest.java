package com.ssafy.yamyam_coach.service.diet_plan.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateDietPlanServiceRequest {

    private Long dietPlanId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String content;

    @Builder
    private UpdateDietPlanServiceRequest(Long dietPlanId, LocalDate startDate, LocalDate endDate, String content) {
        this.dietPlanId = dietPlanId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
    }
}
