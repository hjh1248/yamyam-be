package com.ssafy.yamyam_coach.domain.food;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    private Long id;
    private String name;
    private String category;
    private Double weight;          // 중량(g)
    private Double calorie;         // 칼로리(kcal)
    private Double protein;         // 단백질(g)
    private Double fat;             // 지방(g)
    private Double carbohydrate;    // 탄수화물(g)
    private Double sugar;           // 당(g)
    private Double sodium;          // 나트륨(mg)
    private Double cholesterol;     // 콜레스테롤(mg)
    private Double saturatedFat;    // 포화지방(g)
    private Double transFat;        // 트랜스지방 (g)

    @Builder
    private Food(String name, String category, Double weight, Double calorie, Double protein, Double fat, Double carbohydrate, Double sugar, Double sodium, Double cholesterol, Double saturatedFat, Double transFat) {
        this.name = name;
        this.category = category;
        this.weight = weight;
        this.calorie = calorie;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
        this.sugar = sugar;
        this.sodium = sodium;
        this.cholesterol = cholesterol;
        this.saturatedFat = saturatedFat;
        this.transFat = transFat;
    }
}
