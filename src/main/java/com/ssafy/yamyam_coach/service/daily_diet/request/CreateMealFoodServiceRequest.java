package com.ssafy.yamyam_coach.service.daily_diet.request;

import lombok.Builder;
import lombok.Data;

@Data
public class CreateMealFoodServiceRequest {

    private Long foodId;
    private Double amount;

    @Builder
    private CreateMealFoodServiceRequest(Long foodId, Double amount) {
        this.foodId = foodId;
        this.amount = amount;
    }
}
