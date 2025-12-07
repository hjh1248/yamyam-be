package com.ssafy.yamyam_coach.repository.daily_diet;

import com.ssafy.yamyam_coach.domain.daily_diet.DailyDiet;
import com.ssafy.yamyam_coach.mapper.daily_diet.DailyDietMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class MyBatisDailyDietRepository implements DailyDietRepository {

    private final DailyDietMapper dailyDietMapper;

    @Override
    public int insert(DailyDiet dailyDiet) {
        return dailyDietMapper.insert(dailyDiet);
    }

    @Override
    public boolean existsByDietPlanIdAndDate(Long dietPlanId, LocalDate date) {
        return dailyDietMapper.existsByDietPlanIdAndDate(dietPlanId, date);
    }
}
