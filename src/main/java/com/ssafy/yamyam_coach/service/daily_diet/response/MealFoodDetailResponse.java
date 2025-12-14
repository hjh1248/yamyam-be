package com.ssafy.yamyam_coach.service.daily_diet.response;

import com.ssafy.yamyam_coach.domain.food.BaseUnit;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MealFoodDetailResponse {

    private Long foodId;
    private String foodName;
    private Long mealFoodID;
    private Double quantity;
    private BaseUnit unit;
    private Double energyPer100;        // 에너지(kcal/100단위)
    private Double proteinPer100;       // 단백질(g/100단위)
    private Double fatPer100;           // 지방(g/100단위)
    private Double carbohydratePer100;  // 탄수화물(g/100단위)
    private Double sugarPer100;         // 당(g/100단위)
    private Double sodiumPer100;        // 나트륨(mg/100단위)
    private Double cholesterolPer100;   // 콜레스테롤(mg/100단위)
    private Double saturatedFatPer100;  // 포화지방(g/100단위)
    private Double transFatPer100;      // 트랜스지방(g/100단위)

    @Builder
    private MealFoodDetailResponse(Long foodId, String foodName, Long mealFoodID, Double quantity, BaseUnit unit, Double energyPer100, Double proteinPer100, Double fatPer100, Double carbohydratePer100, Double sugarPer100, Double sodiumPer100, Double cholesterolPer100, Double saturatedFatPer100, Double transFatPer100) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.mealFoodID = mealFoodID;
        this.quantity = quantity;
        this.unit = unit;
        this.energyPer100 = energyPer100;
        this.proteinPer100 = proteinPer100;
        this.fatPer100 = fatPer100;
        this.carbohydratePer100 = carbohydratePer100;
        this.sugarPer100 = sugarPer100;
        this.sodiumPer100 = sodiumPer100;
        this.cholesterolPer100 = cholesterolPer100;
        this.saturatedFatPer100 = saturatedFatPer100;
        this.transFatPer100 = transFatPer100;
    }
}
