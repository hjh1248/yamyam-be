package com.ssafy.yamyam_coach.service.meal.response;

import com.ssafy.yamyam_coach.domain.meals.MealType;
import com.ssafy.yamyam_coach.repository.meal.response.MealDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MealDetailServiceResponse {
    private final Long mealId;
    private final MealType mealType;
    private final Long dailyDietId;
    private final List<MealFoodDetailServiceResponse> mealFoods;

    @Builder
    private MealDetailServiceResponse(Long mealId, MealType mealType, Long dailyDietId, List<MealFoodDetailServiceResponse> mealFoods) {
        this.mealId = mealId;
        this.mealType = mealType;
        this.dailyDietId = dailyDietId;
        this.mealFoods = mealFoods;
    }

    public static MealDetailServiceResponse from(MealDetail mealDetail) {
        return MealDetailServiceResponse.builder()
                .mealId(mealDetail.getId())
                .mealType(mealDetail.getType())
                .dailyDietId(mealDetail.getDailyDietId())
                .mealFoods(mealDetail.getMealFoods().stream()
                        .map(MealFoodDetailServiceResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
