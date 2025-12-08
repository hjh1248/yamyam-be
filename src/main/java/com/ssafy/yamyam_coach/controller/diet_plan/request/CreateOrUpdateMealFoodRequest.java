package com.ssafy.yamyam_coach.controller.diet_plan.request;

import com.ssafy.yamyam_coach.service.daily_diet.request.CreateMealFoodServiceRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateOrUpdateMealFoodRequest {

    @NotNull(message = "음식 id 는 필수 입니다.")
    private Long foodId;

    @NotNull(message = "섭취량은 필수입니다.")
    @Positive(message = "양은 0 초과 값이어야 합니다.")
    private Double amount;

    public CreateMealFoodServiceRequest toServiceRequest() {
        return CreateMealFoodServiceRequest.builder()
                .foodId(foodId)
                .amount(amount)
                .build();
    }

}
