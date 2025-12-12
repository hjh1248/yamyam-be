package com.ssafy.yamyam_coach.service.meal.request;

import com.ssafy.yamyam_coach.domain.meals.MealType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CreateMealServiceRequest {

    private Long dailyDietId;
    private MealType mealType;
    private List<CreateMealFoodRequest> mealFoodRequests;

    @Builder
    private CreateMealServiceRequest(Long dailyDietId, MealType mealType, List<CreateMealFoodRequest> mealFoodRequests) {
        this.dailyDietId = dailyDietId;
        this.mealType = mealType;
        this.mealFoodRequests = mealFoodRequests;
    }
}
