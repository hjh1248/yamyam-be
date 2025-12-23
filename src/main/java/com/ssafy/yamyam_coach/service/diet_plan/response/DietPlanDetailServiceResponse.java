package com.ssafy.yamyam_coach.service.diet_plan.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.yamyam_coach.service.daily_diet.response.DailyDietDetailResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DietPlanDetailServiceResponse {

    private Long dietPlanId;
    private Long authorId;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private boolean isPrimary;

    List<DailyDietDetailResponse> dailyDiets;

    @Builder
    private DietPlanDetailServiceResponse(Long dietPlanId, Long authorId, String title, String content, LocalDate startDate, LocalDate endDate, boolean isPrimary, List<DailyDietDetailResponse> dailyDiets) {
        this.dietPlanId = dietPlanId;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPrimary = isPrimary;
        this.dailyDiets = dailyDiets;
    }
}
