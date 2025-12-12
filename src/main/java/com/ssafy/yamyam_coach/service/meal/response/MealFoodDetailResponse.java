package com.ssafy.yamyam_coach.service.meal.response;

import com.ssafy.yamyam_coach.domain.food.BaseUnit;
import com.ssafy.yamyam_coach.repository.meal.response.MealFoodDetail;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MealFoodDetailResponse {
    private final Long mealFoodId;
    private final Double quantity;
    private final Long foodId;
    private final String foodName;
    private final String category;
    private final BaseUnit baseUnit;
    private final Double energyPer100;
    private final Double proteinPer100;
    private final Double fatPer100;
    private final Double carbohydratePer100;

    @Builder
    private MealFoodDetailResponse(Long mealFoodId, Double quantity, Long foodId, String foodName,
                                          String category, BaseUnit baseUnit, Double energyPer100,
                                          Double proteinPer100, Double fatPer100, Double carbohydratePer100) {
        this.mealFoodId = mealFoodId;
        this.quantity = quantity;
        this.foodId = foodId;
        this.foodName = foodName;
        this.category = category;
        this.baseUnit = baseUnit;
        this.energyPer100 = energyPer100;
        this.proteinPer100 = proteinPer100;
        this.fatPer100 = fatPer100;
        this.carbohydratePer100 = carbohydratePer100;
    }

    public static MealFoodDetailResponse from(MealFoodDetail mealFoodDetail) {
        return MealFoodDetailResponse.builder()
                .mealFoodId(mealFoodDetail.getId())
                .quantity(mealFoodDetail.getQuantity())
                .foodId(mealFoodDetail.getFood().getId())
                .foodName(mealFoodDetail.getFood().getName())
                .category(mealFoodDetail.getFood().getCategory())
                .baseUnit(mealFoodDetail.getFood().getBaseUnit())
                .energyPer100(mealFoodDetail.getFood().getEnergyPer100())
                .proteinPer100(mealFoodDetail.getFood().getProteinPer100())
                .fatPer100(mealFoodDetail.getFood().getFatPer100())
                .carbohydratePer100(mealFoodDetail.getFood().getCarbohydratePer100())
                .build();
    }
}
