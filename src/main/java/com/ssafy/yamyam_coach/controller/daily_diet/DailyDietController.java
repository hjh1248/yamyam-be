package com.ssafy.yamyam_coach.controller.daily_diet;

import com.ssafy.yamyam_coach.controller.daily_diet.request.DailyDietUpdateRequest;
import com.ssafy.yamyam_coach.controller.daily_diet.request.RegisterDailyDietRequest;
import com.ssafy.yamyam_coach.domain.user.User;
import com.ssafy.yamyam_coach.global.annotation.LoginUser;
import com.ssafy.yamyam_coach.service.daily_diet.DailyDietService;
import com.ssafy.yamyam_coach.service.daily_diet.request.DailyDietDetailServiceRequest;
import com.ssafy.yamyam_coach.service.daily_diet.request.DailyDietUpdateServiceRequest;
import com.ssafy.yamyam_coach.service.daily_diet.response.DailyDietDetailResponse;
import com.ssafy.yamyam_coach.service.daily_diet.response.DailyDietsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/diet-plans/{dietPlanId}/daily-diets")
@RequiredArgsConstructor
public class DailyDietController {

    private final DailyDietService dailyDietService;

    @PostMapping
    public ResponseEntity<Void> registerDailyDiet(@LoginUser User currentUser, @RequestBody @Valid RegisterDailyDietRequest request) {
        Long currentUserId = currentUser.getId();
        log.debug("[DailyDietController.registerDailyDiet]: daily diet 등록 요청! user id = {}", currentUserId);

        Long createdDailyDietId = dailyDietService.registerDailyDiet(currentUserId, request.toServiceRequest());
        log.debug("[DailyDietController.registerDailyDiet]: createdDailyDietId: {}", createdDailyDietId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdDailyDietId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<DailyDietsResponse> getDailyDietByDietPlan(@PathVariable Long dietPlanId) {
        log.debug("[DailyDietController.getDailyDietByDietPlan]: diet plan 에 속한 daily diet 요청! diet plan id = {}", dietPlanId);
        return ResponseEntity.ok(dailyDietService.getDailyDietByDietPlan(dietPlanId));
    }

    @GetMapping("/{date}")
    public ResponseEntity<DailyDietDetailResponse> getDailyDietDetailByDietPlanAndDate(@PathVariable Long dietPlanId, @PathVariable LocalDate date) {

        log.debug("[DailyDietController.getDailyDietDetailByDietPlanAndDate]: diet plan 에 속한 daily diet 요청! diet plan id = {} date = {}", dietPlanId, date);
        DailyDietDetailServiceRequest request = DailyDietDetailServiceRequest.builder()
                .dietPlanId(dietPlanId)
                .date(date)
                .build();

        return ResponseEntity.ok(dailyDietService.getDailyDietDetailByDietPlan(request));
    }

    @PatchMapping("/{dailyDietId}")
    public ResponseEntity<Void> updateDailyDiet(@LoginUser User currentUser, @PathVariable Long dailyDietId, @RequestBody DailyDietUpdateRequest request) {
        Long currentUserId = currentUser.getId();
        log.debug("[DailyDietController.updateDescription]: daily diet update 요청! daily diet id = {}, userId = {}", dailyDietId, currentUserId);

        DailyDietUpdateServiceRequest serviceRequest = DailyDietUpdateServiceRequest.builder()
                .dailyDietId(dailyDietId)
                .date(request.getNewDate())
                .description(request.getNewDescription())
                .build();

        dailyDietService.updateDailyDiet(currentUserId, serviceRequest);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{dailyDietId}")
    public ResponseEntity<Void> deleteDailyDiet(@LoginUser User currentUser, @PathVariable Long dailyDietId) {
        Long currentUserId = currentUser.getId();
        log.debug("[DailyDietController.deleteDailyDiet]: daily diet delete 요청! daily diet id = {}, userId = {}", dailyDietId, currentUserId);

        dailyDietService.deleteDailyDiet(currentUserId, dailyDietId);

        return ResponseEntity.noContent().build();
    }
}
