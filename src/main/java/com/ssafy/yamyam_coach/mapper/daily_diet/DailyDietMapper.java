package com.ssafy.yamyam_coach.mapper.daily_diet;

import com.ssafy.yamyam_coach.domain.daily_diet.DailyDiet;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

@Mapper
public interface DailyDietMapper {

    int insert(DailyDiet dailyDiet);

    boolean existsByDietPlanIdAndDate(Long dietPlanId, LocalDate date);
}
