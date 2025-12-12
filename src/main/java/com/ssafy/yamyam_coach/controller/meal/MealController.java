package com.ssafy.yamyam_coach.controller.meal;

import com.ssafy.yamyam_coach.controller.meal.request.CreateMealFoodRequest;
import com.ssafy.yamyam_coach.controller.meal.request.CreateMealRequest;
import com.ssafy.yamyam_coach.controller.meal.request.UpdateMealFoodRequest;
import com.ssafy.yamyam_coach.controller.meal.request.UpdateMealRequest;
import com.ssafy.yamyam_coach.service.meal.MealService;
import com.ssafy.yamyam_coach.service.meal.request.CreateMealServiceRequest;
import com.ssafy.yamyam_coach.service.meal.request.UpdateMealServiceRequest;
import com.ssafy.yamyam_coach.service.meal.response.MealDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/diet-plans/{dietPlanId}/daily-diets/{dailyDietId}/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @PostMapping
    public ResponseEntity<Void> registerMeal(@PathVariable Long dailyDietId, @RequestBody @Valid CreateMealRequest request) {
        Long currentUserId = 1L;

        log.debug("[MealController.registerMeal] meal 등록 요청! dailyDietId: {}", dailyDietId);

        CreateMealServiceRequest serviceRequest = CreateMealServiceRequest.builder()
                .dailyDietId(dailyDietId)
                .mealType(request.getMealType())
                .mealFoodRequests(request.getMealFoodRequests() == null ? List.of()
                        : request.getMealFoodRequests().stream().map(CreateMealFoodRequest::toServiceRequest).toList())
                .build();

        Long createdMealId = mealService.createMeal(currentUserId, serviceRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdMealId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{mealId}")
    public ResponseEntity<MealDetailResponse> getMeal(@PathVariable Long mealId) {
        log.debug("[MealController.getMeal] meal 조회 요청! mealId: {}", mealId);
        return ResponseEntity.ok(mealService.getMealById(mealId));
    }

    @PatchMapping("/{mealId}")
    public ResponseEntity<Void> updateMeal(@PathVariable Long mealId, @RequestBody @Valid UpdateMealRequest request) {
        Long currentUserId = 1L;

        log.debug("[MealController.updateMeal] meal update 요청! mealId: {}", mealId);

        UpdateMealServiceRequest serviceRequest = UpdateMealServiceRequest.builder()
                .mealId(mealId)
                .mealType(request.getMealType())
                .mealFoodUpdateRequests(request.getMealFoodUpdateRequests() == null ? List.of()
                        : request.getMealFoodUpdateRequests().stream().map(UpdateMealFoodRequest::toServiceRequest).toList())
                .build();

        mealService.updateMeal(currentUserId, serviceRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long mealId) {
        Long currentUserId = 1L;
        log.debug("[MealController.deleteMeal] meal delete 요청! mealId: {}", mealId);
        mealService.deleteMeal(currentUserId, mealId);
        return ResponseEntity.noContent().build();
    }
}
