package com.ssafy.yamyam_coach.controller.meal.request;

import com.ssafy.yamyam_coach.service.meal.request.UpdateMealFoodServiceRequest;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateMealFoodRequest {

    private Long foodId;
    private Double amount;

    public UpdateMealFoodServiceRequest toServiceRequest() {
        return UpdateMealFoodServiceRequest.builder()
                .foodId(foodId)
                .amount(amount)
                .build();
    }

    @Builder
    private UpdateMealFoodRequest(Long foodId, Double amount) {
        this.foodId = foodId;
        this.amount = amount;
    }
}
