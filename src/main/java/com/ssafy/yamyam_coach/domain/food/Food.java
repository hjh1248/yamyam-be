package com.ssafy.yamyam_coach.domain.food;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    private Long id;
    private String name;
    private String category;
    private BaseUnit baseUnit;

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
    private Food(String name, String category, BaseUnit baseUnit, Double energyPer100, Double proteinPer100, Double fatPer100, Double carbohydratePer100, Double sugarPer100, Double sodiumPer100, Double cholesterolPer100, Double saturatedFatPer100, Double transFatPer100) {
        this.name = name;
        this.category = category;
        this.baseUnit = baseUnit;
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
