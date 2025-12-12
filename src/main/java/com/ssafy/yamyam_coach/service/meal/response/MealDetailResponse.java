package com.ssafy.yamyam_coach.service.meal.response;

import com.ssafy.yamyam_coach.domain.meals.MealType;
import com.ssafy.yamyam_coach.repository.meal.response.MealDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MealDetailResponse {
    private final Long mealId;
    private final MealType mealType;
    private final Long dailyDietId;
    private final List<MealFoodDetailResponse> mealFoods;

    @Builder
    private MealDetailResponse(Long mealId, MealType mealType, Long dailyDietId, List<MealFoodDetailResponse> mealFoods) {
        this.mealId = mealId;
        this.mealType = mealType;
        this.dailyDietId = dailyDietId;
        this.mealFoods = mealFoods;
    }
}
