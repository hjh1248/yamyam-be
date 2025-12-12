package com.ssafy.yamyam_coach.service.meal;

import com.ssafy.yamyam_coach.IntegrationTestSupport;
import com.ssafy.yamyam_coach.domain.daily_diet.DailyDiet;
import com.ssafy.yamyam_coach.domain.dietplan.DietPlan;
import com.ssafy.yamyam_coach.domain.food.Food;
import com.ssafy.yamyam_coach.domain.mealfood.MealFood;
import com.ssafy.yamyam_coach.domain.meals.Meal;
import com.ssafy.yamyam_coach.domain.meals.MealType;
import com.ssafy.yamyam_coach.domain.user.User;
import com.ssafy.yamyam_coach.exception.daily_diet.DailyDietException;
import com.ssafy.yamyam_coach.exception.diet_plan.DietPlanException;
import com.ssafy.yamyam_coach.exception.food.FoodException;
import com.ssafy.yamyam_coach.exception.meal.MealException;
import com.ssafy.yamyam_coach.repository.daily_diet.DailyDietRepository;
import com.ssafy.yamyam_coach.repository.diet_plan.DietPlanRepository;
import com.ssafy.yamyam_coach.repository.food.FoodRepository;
import com.ssafy.yamyam_coach.repository.meal.MealRepository;
import com.ssafy.yamyam_coach.repository.mealfood.MealFoodRepository;
import com.ssafy.yamyam_coach.repository.user.UserRepository;
import com.ssafy.yamyam_coach.service.meal.request.CreateMealFoodRequest;
import com.ssafy.yamyam_coach.service.meal.request.CreateMealServiceRequest;
import com.ssafy.yamyam_coach.service.meal.request.UpdateMealFoodServiceRequest;
import com.ssafy.yamyam_coach.service.meal.request.UpdateMealServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static com.ssafy.yamyam_coach.util.TestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MealServiceTest extends IntegrationTestSupport {

    @Autowired
    MealService mealService;

    @Autowired
    MealRepository mealRepository;

    @Autowired
    MealFoodRepository mealFoodRepository;

    @Autowired
    DailyDietRepository dailyDietRepository;

    @Autowired
    DietPlanRepository dietPlanRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FoodRepository foodRepository;

    @Nested
    @DisplayName("createMeal")
    class CreateMeal {

        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase {

            @Test
            @DisplayName("유효한 요청으로 meal과 meal_food를 정상적으로 생성한다")
            void createMealSuccessfully() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(7));
                dietPlanRepository.insert(dietPlan);

                DailyDiet dailyDiet = createDailyDiet(dietPlan.getId(), LocalDate.now(), "오늘의 식단");
                dailyDietRepository.insert(dailyDiet);

                Food food1 = createDummyFoodByName("닭가슴살");
                Food food2 = createDummyFoodByName("현미밥");
                foodRepository.insert(food1);
                foodRepository.insert(food2);

                CreateMealFoodRequest mealFood1 = new CreateMealFoodRequest();
                mealFood1.setFoodId(food1.getId());
                mealFood1.setAmount(200.0);

                CreateMealFoodRequest mealFood2 = new CreateMealFoodRequest();
                mealFood2.setFoodId(food2.getId());
                mealFood2.setAmount(150.0);

                CreateMealServiceRequest request = CreateMealServiceRequest.builder()
                        .dailyDietId(dailyDiet.getId())
                        .mealType(MealType.BREAKFAST)
                        .mealFoodRequests(List.of(mealFood1, mealFood2))
                        .build();

                // when
                mealService.createMeal(user.getId(), request);

                // then
                Meal findMeal = mealRepository.findByDailyDietAndMealType(dailyDiet.getId(), MealType.BREAKFAST).orElse(null);
                assertThat(findMeal).isNotNull();
                List<MealFood> mealFoods = mealFoodRepository.findByMeal(findMeal.getId());
                assertThat(mealFoods).hasSize(2)
                        .extracting(MealFood::getFoodId)
                        .containsExactlyInAnyOrder(mealFood1.getFoodId(), mealFood2.getFoodId());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailureCase {

            @Test
            @DisplayName("존재하지 않는 dailyDiet으로 meal 생성 시 NOT_FOUND_DAILY_DIET 예외가 발생한다")
            void createMealWithNotExistingDailyDiet() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                Long notExistingDailyDietId = 99999L;

                CreateMealServiceRequest request = CreateMealServiceRequest.builder()
                        .dailyDietId(notExistingDailyDietId)
                        .mealType(MealType.BREAKFAST)
                        .mealFoodRequests(List.of())
                        .build();

                // when & then
                assertThatThrownBy(() -> mealService.createMeal(user.getId(), request))
                        .isInstanceOf(DailyDietException.class)
                        .hasMessage("해당 일일 식단을 조회할 수 없습니다.");
            }

            @Test
            @DisplayName("다른 사용자의 dietPlan에 meal 생성 시도 시 UNAUTHORIZED 예외가 발생한다")
            void createMealWithOtherUsersDietPlan() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                User otherUser = createUser("다른사람", "다른닉네임", "other@example.com", "password");
                userRepository.save(otherUser);

                DietPlan othersDietPlan = createDummyDietPlan(otherUser.getId(), LocalDate.now(), LocalDate.now().plusDays(7));
                dietPlanRepository.insert(othersDietPlan);

                DailyDiet othersDailyDiet = createDailyDiet(othersDietPlan.getId(), LocalDate.now(), "다른 사람의 식단");
                dailyDietRepository.insert(othersDailyDiet);

                CreateMealServiceRequest request = CreateMealServiceRequest.builder()
                        .dailyDietId(othersDailyDiet.getId())
                        .mealType(MealType.BREAKFAST)
                        .mealFoodRequests(List.of())
                        .build();

                // when & then
                assertThatThrownBy(() -> mealService.createMeal(user.getId(), request))
                        .isInstanceOf(MealException.class)
                        .hasMessage("식사를 생성할 권한이 없습니다.");
            }

            @Test
            @DisplayName("이미 같은 mealType이 존재하는 dailyDiet에 meal 생성 시 DUPLICATED_MEAL_TYPE 예외가 발생한다")
            void createMealWithDuplicatedMealType() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(7));
                dietPlanRepository.insert(dietPlan);

                DailyDiet dailyDiet = createDailyDiet(dietPlan.getId(), LocalDate.now(), "오늘의 식단");
                dailyDietRepository.insert(dailyDiet);

                // 이미 아침 식사가 존재
                Meal existingBreakfast = createMeal(dailyDiet.getId(), MealType.BREAKFAST);
                mealRepository.insert(existingBreakfast);

                CreateMealServiceRequest request = CreateMealServiceRequest.builder()
                        .dailyDietId(dailyDiet.getId())
                        .mealType(MealType.BREAKFAST)
                        .mealFoodRequests(List.of())
                        .build();

                // when & then
                assertThatThrownBy(() -> mealService.createMeal(user.getId(), request))
                        .isInstanceOf(MealException.class)
                        .hasMessage("이미 해당 타입의 식사가 존재합니다.");
            }

            @Test
            @DisplayName("존재하지 않는 food로 meal 생성 시 NOT_FOUND_FOOD 예외가 발생한다")
            void createMealWithNotExistingFood() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(7));
                dietPlanRepository.insert(dietPlan);

                DailyDiet dailyDiet = createDailyDiet(dietPlan.getId(), LocalDate.now(), "오늘의 식단");
                dailyDietRepository.insert(dailyDiet);

                Long notExistingFoodId = 99999L;

                CreateMealFoodRequest mealFood = new CreateMealFoodRequest();
                mealFood.setFoodId(notExistingFoodId);
                mealFood.setAmount(200.0);

                CreateMealServiceRequest request = CreateMealServiceRequest.builder()
                        .dailyDietId(dailyDiet.getId())
                        .mealType(MealType.BREAKFAST)
                        .mealFoodRequests(List.of(mealFood))
                        .build();

                // when & then
                assertThatThrownBy(() -> mealService.createMeal(user.getId(), request))
                        .isInstanceOf(FoodException.class)
                        .hasMessage("해당 음식을 조회할 수 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("updateMeal")
    class UpdateMeal {

        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase {

            @Test
            @DisplayName("meal type과 meal_food를 정상적으로 수정한다")
            void updateMealTypeAndMealFoods() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(7));
                dietPlanRepository.insert(dietPlan);

                DailyDiet dailyDiet = createDailyDiet(dietPlan.getId(), LocalDate.now(), "오늘의 식단");
                dailyDietRepository.insert(dailyDiet);

                Meal breakfast = createMeal(dailyDiet.getId(), MealType.BREAKFAST);
                mealRepository.insert(breakfast);

                Food oldFood = createDummyFoodByName("닭가슴살");
                foodRepository.insert(oldFood);

                MealFood oldMealFood = createMealFood(breakfast.getId(), oldFood.getId(), 100.0);
                mealFoodRepository.insert(oldMealFood);

                // 새로운 음식들
                Food newFood1 = createDummyFoodByName("연어");
                Food newFood2 = createDummyFoodByName("현미밥");
                foodRepository.insert(newFood1);
                foodRepository.insert(newFood2);

                UpdateMealFoodServiceRequest updateMealFood1 = new UpdateMealFoodServiceRequest();
                updateMealFood1.setFoodId(newFood1.getId());
                updateMealFood1.setAmount(150.0);

                UpdateMealFoodServiceRequest updateMealFood2 = new UpdateMealFoodServiceRequest();
                updateMealFood2.setFoodId(newFood2.getId());
                updateMealFood2.setAmount(200.0);

                UpdateMealServiceRequest request = new UpdateMealServiceRequest();
                request.setMealId(breakfast.getId());
                request.setMealType(MealType.LUNCH); // BREAKFAST → LUNCH 변경
                request.setMealFoodUpdateRequests(List.of(updateMealFood1, updateMealFood2));

                // when
                mealService.updateMeal(user.getId(), request);

                // then
                Meal updatedMeal = mealRepository.findById(breakfast.getId()).orElse(null);
                assertThat(updatedMeal).isNotNull();
                assertThat(updatedMeal.getType()).isEqualTo(MealType.LUNCH);

                List<MealFood> updatedMealFoods = mealFoodRepository.findByMeal(breakfast.getId());
                assertThat(updatedMealFoods).hasSize(2)
                        .extracting(MealFood::getFoodId)
                        .containsExactlyInAnyOrder(newFood1.getId(), newFood2.getId());
            }

            @Test
            @DisplayName("meal type은 그대로 두고 meal_food만 변경한다")
            void updateOnlyMealFoods() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(7));
                dietPlanRepository.insert(dietPlan);

                DailyDiet dailyDiet = createDailyDiet(dietPlan.getId(), LocalDate.now(), "오늘의 식단");
                dailyDietRepository.insert(dailyDiet);

                Meal breakfast = createMeal(dailyDiet.getId(), MealType.BREAKFAST);
                mealRepository.insert(breakfast);

                Food oldFood = createDummyFoodByName("닭가슴살");
                foodRepository.insert(oldFood);

                MealFood oldMealFood = createMealFood(breakfast.getId(), oldFood.getId(), 100.0);
                mealFoodRepository.insert(oldMealFood);

                Food newFood = createDummyFoodByName("연어");
                foodRepository.insert(newFood);

                UpdateMealFoodServiceRequest updateMealFood = new UpdateMealFoodServiceRequest();
                updateMealFood.setFoodId(newFood.getId());
                updateMealFood.setAmount(150.0);

                UpdateMealServiceRequest request = new UpdateMealServiceRequest();
                request.setMealId(breakfast.getId());
                request.setMealType(MealType.BREAKFAST); // 그대로 BREAKFAST
                request.setMealFoodUpdateRequests(List.of(updateMealFood));

                // when
                mealService.updateMeal(user.getId(), request);

                // then
                Meal updatedMeal = mealRepository.findById(breakfast.getId()).orElse(null);
                assertThat(updatedMeal).isNotNull();
                assertThat(updatedMeal.getType()).isEqualTo(MealType.BREAKFAST);

                List<MealFood> updatedMealFoods = mealFoodRepository.findByMeal(breakfast.getId());
                assertThat(updatedMealFoods).hasSize(1);
                assertThat(updatedMealFoods.get(0).getFoodId()).isEqualTo(newFood.getId());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailureCase {

            @Test
            @DisplayName("존재하지 않는 meal을 수정하려 할 때 NOT_FOUND_MEAL 예외가 발생한다")
            void updateNotExistingMeal() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                Long notExistingMealId = 99999L;

                UpdateMealServiceRequest request = new UpdateMealServiceRequest();
                request.setMealId(notExistingMealId);
                request.setMealType(MealType.BREAKFAST);
                request.setMealFoodUpdateRequests(List.of());

                // when & then
                assertThatThrownBy(() -> mealService.updateMeal(user.getId(), request))
                        .isInstanceOf(MealException.class)
                        .hasMessage("해당 식사를 조회할 수 없습니다.");
            }

            @Test
            @DisplayName("다른 사용자의 meal을 수정하려 할 때 UNAUTHORIZED 예외가 발생한다")
            void updateOtherUsersMeal() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                User otherUser = createUser("다른사람", "다른닉네임", "other@example.com", "password");
                userRepository.save(otherUser);

                DietPlan othersDietPlan = createDummyDietPlan(otherUser.getId(), LocalDate.now(), LocalDate.now().plusDays(7));
                dietPlanRepository.insert(othersDietPlan);

                DailyDiet othersDailyDiet = createDailyDiet(othersDietPlan.getId(), LocalDate.now(), "다른 사람의 식단");
                dailyDietRepository.insert(othersDailyDiet);

                Meal othersMeal = createMeal(othersDailyDiet.getId(), MealType.BREAKFAST);
                mealRepository.insert(othersMeal);

                UpdateMealServiceRequest request = new UpdateMealServiceRequest();
                request.setMealId(othersMeal.getId());
                request.setMealType(MealType.LUNCH);
                request.setMealFoodUpdateRequests(List.of());

                // when & then
                assertThatThrownBy(() -> mealService.updateMeal(user.getId(), request))
                        .isInstanceOf(MealException.class)
                        .hasMessage("식사를 생성할 권한이 없습니다.");
            }

            @Test
            @DisplayName("변경하려는 meal type이 이미 존재할 때 DUPLICATED_MEAL_TYPE 예외가 발생한다")
            void updateMealTypeToExistingType() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(7));
                dietPlanRepository.insert(dietPlan);

                DailyDiet dailyDiet = createDailyDiet(dietPlan.getId(), LocalDate.now(), "오늘의 식단");
                dailyDietRepository.insert(dailyDiet);

                Meal breakfast = createMeal(dailyDiet.getId(), MealType.BREAKFAST);
                mealRepository.insert(breakfast);

                // 이미 점심 식사가 존재
                Meal lunch = createMeal(dailyDiet.getId(), MealType.LUNCH);
                mealRepository.insert(lunch);

                UpdateMealServiceRequest request = new UpdateMealServiceRequest();
                request.setMealId(breakfast.getId());
                request.setMealType(MealType.LUNCH); // BREAKFAST → LUNCH (이미 존재)
                request.setMealFoodUpdateRequests(List.of());

                // when & then
                assertThatThrownBy(() -> mealService.updateMeal(user.getId(), request))
                        .isInstanceOf(MealException.class)
                        .hasMessage("이미 해당 타입의 식사가 존재합니다.");
            }

            @Test
            @DisplayName("존재하지 않는 food로 meal 수정 시 NOT_FOUND_FOOD 예외가 발생한다")
            void updateMealWithNotExistingFood() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(7));
                dietPlanRepository.insert(dietPlan);

                DailyDiet dailyDiet = createDailyDiet(dietPlan.getId(), LocalDate.now(), "오늘의 식단");
                dailyDietRepository.insert(dailyDiet);

                Meal breakfast = createMeal(dailyDiet.getId(), MealType.BREAKFAST);
                mealRepository.insert(breakfast);

                Long notExistingFoodId = 99999L;

                UpdateMealFoodServiceRequest updateMealFood = new UpdateMealFoodServiceRequest();
                updateMealFood.setFoodId(notExistingFoodId);
                updateMealFood.setAmount(200.0);

                UpdateMealServiceRequest request = new UpdateMealServiceRequest();
                request.setMealId(breakfast.getId());
                request.setMealType(MealType.BREAKFAST);
                request.setMealFoodUpdateRequests(List.of(updateMealFood));

                // when & then
                assertThatThrownBy(() -> mealService.updateMeal(user.getId(), request))
                        .isInstanceOf(FoodException.class)
                        .hasMessage("해당 음식을 조회할 수 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("deleteMeal")
    class DeleteMeal {

        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase {

            @Test
            @DisplayName("meal을 정상적으로 삭제한다 (meal_food도 cascade로 삭제)")
            void deleteMealSuccessfully() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(7));
                dietPlanRepository.insert(dietPlan);

                DailyDiet dailyDiet = createDailyDiet(dietPlan.getId(), LocalDate.now(), "오늘의 식단");
                dailyDietRepository.insert(dailyDiet);

                Meal breakfast = createMeal(dailyDiet.getId(), MealType.BREAKFAST);
                mealRepository.insert(breakfast);

                Food food = createDummyFoodByName("닭가슴살");
                foodRepository.insert(food);

                MealFood mealFood = createMealFood(breakfast.getId(), food.getId(), 100.0);
                mealFoodRepository.insert(mealFood);

                // when
                mealService.deleteMeal(user.getId(), breakfast.getId());

                // then
                Meal deletedMeal = mealRepository.findById(breakfast.getId()).orElse(null);
                assertThat(deletedMeal).isNull();

                // cascade로 meal_food도 삭제되었는지 확인
                List<MealFood> mealFoods = mealFoodRepository.findByMeal(breakfast.getId());
                assertThat(mealFoods).isEmpty();
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailureCase {

            @Test
            @DisplayName("존재하지 않는 meal을 삭제하려 할 때 NOT_FOUND_MEAL 예외가 발생한다")
            void deleteNotExistingMeal() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                Long notExistingMealId = 99999L;

                // when & then
                assertThatThrownBy(() -> mealService.deleteMeal(user.getId(), notExistingMealId))
                        .isInstanceOf(MealException.class)
                        .hasMessage("해당 식사를 조회할 수 없습니다.");
            }

            @Test
            @DisplayName("다른 사용자의 meal을 삭제하려 할 때 UNAUTHORIZED 예외가 발생한다")
            void deleteOtherUsersMeal() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                User otherUser = createUser("다른사람", "다른닉네임", "other@example.com", "password");
                userRepository.save(otherUser);

                DietPlan othersDietPlan = createDummyDietPlan(otherUser.getId(), LocalDate.now(), LocalDate.now().plusDays(7));
                dietPlanRepository.insert(othersDietPlan);

                DailyDiet othersDailyDiet = createDailyDiet(othersDietPlan.getId(), LocalDate.now(), "다른 사람의 식단");
                dailyDietRepository.insert(othersDailyDiet);

                Meal othersMeal = createMeal(othersDailyDiet.getId(), MealType.BREAKFAST);
                mealRepository.insert(othersMeal);

                // when & then
                assertThatThrownBy(() -> mealService.deleteMeal(user.getId(), othersMeal.getId()))
                        .isInstanceOf(MealException.class)
                        .hasMessage("식사를 생성할 권한이 없습니다.");
            }
        }
    }
}
