package com.ssafy.yamyam_coach.service.diet_plan.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DietPlanServiceResponse {

    private Long dietPlanId;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private boolean isPrimary;

    @Builder
    private DietPlanServiceResponse(Long dietPlanId, String title, String content, LocalDate startDate, LocalDate endDate, boolean isPrimary) {
        this.dietPlanId = dietPlanId;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPrimary = isPrimary;
    }
}
