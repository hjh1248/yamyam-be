package com.ssafy.yamyam_coach.controller.meal.request;

import com.ssafy.yamyam_coach.service.meal.request.CreateMealFoodServiceRequest;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateMealFoodRequest {

    private Long foodId;
    private Double amount;

    public CreateMealFoodServiceRequest toServiceRequest() {
        return CreateMealFoodServiceRequest.builder()
                .foodId(foodId)
                .amount(amount)
                .build();
    }

    @Builder
    private CreateMealFoodRequest(Long foodId, Double amount) {
        this.foodId = foodId;
        this.amount = amount;
    }
}
