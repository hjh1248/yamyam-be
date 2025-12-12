package com.ssafy.yamyam_coach.controller.meal.request;

import com.ssafy.yamyam_coach.domain.meals.MealType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CreateMealRequest {

    @NotNull(message = "식사 타입은 필수입니다.")
    private MealType mealType;

    @NotNull(message = "음식 목록은 필수입니다.")
    private List<CreateMealFoodRequest> mealFoodRequests;

    @Builder
    private CreateMealRequest(MealType mealType, List<CreateMealFoodRequest> mealFoodRequests) {
        this.mealType = mealType;
        this.mealFoodRequests = mealFoodRequests;
    }
}
