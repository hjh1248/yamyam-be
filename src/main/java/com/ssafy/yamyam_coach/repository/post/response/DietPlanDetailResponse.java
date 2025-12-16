package com.ssafy.yamyam_coach.repository.post.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DietPlanDetailResponse {

    private Long dietPlanId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

}
