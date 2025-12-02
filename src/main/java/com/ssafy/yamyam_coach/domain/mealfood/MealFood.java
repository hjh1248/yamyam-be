package com.ssafy.yamyam_coach.domain.mealfood;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MealFood {

    private Long id;
    private Long mealId;
    private Long foodId;
    private Double quantity;

    @Builder
    private MealFood(Long mealId, Long foodId, Double quantity) {
        this.mealId = mealId;
        this.foodId = foodId;
        this.quantity = quantity;
    }
}
