package com.ssafy.yamyam_coach.service.meal.request;

import com.ssafy.yamyam_coach.domain.meals.MealType;
import lombok.Data;

import java.util.List;

@Data
public class UpdateMealServiceRequest{

    private Long mealId;
    private MealType mealType;
    private List<UpdateMealFoodServiceRequest> mealFoodUpdateRequests;

}
