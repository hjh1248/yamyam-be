package com.ssafy.yamyam_coach.mapper.meal;

import com.ssafy.yamyam_coach.domain.meals.Meal;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MealMapper {

    int insert(Meal meal);

}
