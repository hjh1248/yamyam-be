package com.ssafy.yamyam_coach.controller.meal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/diet-plans/{dietPlanId}/daily-diets/{dailyDietId}/meals")
@RequiredArgsConstructor
public class MealController {

//    @PostMapping
//    public ResponseEntity<Void> registerMeal(@PathVariable Long dailyDietId) {
//
//    }
}
