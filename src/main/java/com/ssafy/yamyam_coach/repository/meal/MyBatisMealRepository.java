package com.ssafy.yamyam_coach.repository.meal;

import com.ssafy.yamyam_coach.domain.meals.Meal;
import com.ssafy.yamyam_coach.mapper.meal.MealMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MyBatisMealRepository implements MealRepository {

    private final MealMapper mealMapper;

    @Override
    public int insert(Meal meal) {
        return mealMapper.insert(meal);
    }
}
