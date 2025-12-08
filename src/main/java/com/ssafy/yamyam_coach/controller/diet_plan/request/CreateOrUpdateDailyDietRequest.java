package com.ssafy.yamyam_coach.controller.diet_plan.request;

import com.ssafy.yamyam_coach.service.daily_diet.request.CreateOrUpdateDailyDietServiceRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateOrUpdateDailyDietRequest {

    @NotNull(message = "날짜 값은 필수 입니다.")
    private LocalDate date;

    @NotBlank(message = "설명은 필수 입니다.")
    private String description;

    @Valid
    @NotNull(message = "아침 식단은 null 이 될 수 없습니다.")
    private List<CreateOrUpdateMealFoodRequest> breakfast;

    @Valid
    @NotNull(message = "점심 식단은 null 이 될 수 없습니다.")
    private List<CreateOrUpdateMealFoodRequest> lunch;

    @Valid
    @NotNull(message = "저녁 식단은 null 이 될 수 없습니다.")
    private List<CreateOrUpdateMealFoodRequest> dinner;

    @Valid
    @NotNull(message = "간식 식단은 null 이 될 수 없습니다.")
    private List<CreateOrUpdateMealFoodRequest> snack;

    public CreateOrUpdateDailyDietServiceRequest toServiceRequest() {
        return CreateOrUpdateDailyDietServiceRequest.builder()
                .date(date)
                .description(description)
                .breakfast(breakfast.stream().map(CreateOrUpdateMealFoodRequest::toServiceRequest).toList())
                .lunch(lunch.stream().map(CreateOrUpdateMealFoodRequest::toServiceRequest).toList())
                .dinner(dinner.stream().map(CreateOrUpdateMealFoodRequest::toServiceRequest).toList())
                .snack(snack.stream().map(CreateOrUpdateMealFoodRequest::toServiceRequest).toList())
                .build();
    }

}
