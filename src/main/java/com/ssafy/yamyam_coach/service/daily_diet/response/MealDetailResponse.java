package com.ssafy.yamyam_coach.service.daily_diet.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class MealDetailResponse {

    private Long mealId;
    private List<MealFoodDetailResponse> mealFoods;

    @Builder
    private MealDetailResponse(Long mealId, List<MealFoodDetailResponse> mealFoods) {
        this.mealId = mealId;
        this.mealFoods = mealFoods;
    }
}
