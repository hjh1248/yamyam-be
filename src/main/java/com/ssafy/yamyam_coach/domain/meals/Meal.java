package com.ssafy.yamyam_coach.domain.meals;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Meal {

    private Long id;
    private Long dailyDietId;
    private MealType type;

    @Builder
    private Meal(Long dailyDietId, MealType type) {
        this.dailyDietId = dailyDietId;
        this.type = type;
    }
}
