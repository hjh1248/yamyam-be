package com.ssafy.yamyam_coach.repository.mealfood;

import com.ssafy.yamyam_coach.domain.mealfood.MealFood;

import java.util.List;

public interface MealFoodRepository {

    int insert(MealFood mealFood);

    int batchInsert(List<MealFood> mealFoods);
}
