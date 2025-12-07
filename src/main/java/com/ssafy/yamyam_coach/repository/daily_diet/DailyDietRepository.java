package com.ssafy.yamyam_coach.repository.daily_diet;

import com.ssafy.yamyam_coach.domain.daily_diet.DailyDiet;

import java.time.LocalDate;

public interface DailyDietRepository {

    int insert(DailyDiet  dailyDiet);

    boolean existsByDietPlanIdAndDate(Long dietPlanId, LocalDate date);
}
