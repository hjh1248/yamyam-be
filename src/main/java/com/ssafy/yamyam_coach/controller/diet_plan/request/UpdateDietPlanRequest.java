package com.ssafy.yamyam_coach.controller.diet_plan.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateDietPlanRequest {

    private LocalDate startDate;

    private LocalDate endDate;

    private String content;

}
