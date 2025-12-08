package com.ssafy.yamyam_coach.service.daily_diet.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateOrUpdateDailyDietServiceRequest {

    private Long dietPlanId;
    private LocalDate date;
    private String description;
    private List<CreateMealFoodServiceRequest> breakfast;
    private List<CreateMealFoodServiceRequest> lunch;
    private List<CreateMealFoodServiceRequest> dinner;
    private List<CreateMealFoodServiceRequest> snack;

    @Builder
    private CreateOrUpdateDailyDietServiceRequest(Long dietPlanId, LocalDate date, String description, List<CreateMealFoodServiceRequest> breakfast, List<CreateMealFoodServiceRequest> lunch, List<CreateMealFoodServiceRequest> dinner, List<CreateMealFoodServiceRequest> snack) {
        this.dietPlanId = dietPlanId;
        this.date = date;
        this.description = description;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.snack = snack;
    }
}
