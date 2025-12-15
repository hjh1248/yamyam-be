package com.ssafy.yamyam_coach.repository.diet_plan;

import com.ssafy.yamyam_coach.IntegrationTestSupport;
import com.ssafy.yamyam_coach.domain.dietplan.DietPlan;
import com.ssafy.yamyam_coach.domain.user.User;
import com.ssafy.yamyam_coach.repository.diet_plan.request.UpdateDietPlanRepositoryRequest;
import com.ssafy.yamyam_coach.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.ssafy.yamyam_coach.util.DomainAssertions.assertDietPlanEquals;
import static com.ssafy.yamyam_coach.util.TestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class DietPlanRepositoryTest extends IntegrationTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DietPlanRepository dietPlanRepository;

    @DisplayName("식단 계획을 저장 할 수 있다.")
    @Test
    void insert() {
        // given
        User user = createUser("test user", "test nickname", "test@email.com", "password");
        userRepository.save(user);

        DietPlan dietPlan = createDietPlan(user.getId(), "title", "content", false, false, LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan);

        // when
        Optional<DietPlan> findDietPlanOpt = dietPlanRepository.findById(dietPlan.getId());

        // then
        assertThat(findDietPlanOpt).isPresent();

        DietPlan findDietPlan = findDietPlanOpt.get();
        assertDietPlanEquals(findDietPlan, dietPlan);
    }

    @DisplayName("사용자 pk 기반으로 식단 계획들을 조회할 수 있다.")
    @Test
    void findDietPlansByUserId() {

        // given
        User user = createUser("test user", "test nickname", "test@email.com", "password");
        userRepository.save(user);

        DietPlan dietPlan1 = createDietPlan(user.getId(), "title", "content", false, true, LocalDate.now(), LocalDate.now().plusDays(1));
        DietPlan dietPlan2 = createDietPlan(user.getId(), "title2", "content2", false, false, LocalDate.now(), LocalDate.now().plusDays(1));

        dietPlanRepository.insert(dietPlan1);
        dietPlanRepository.insert(dietPlan2);

        // when
        List<DietPlan> plans = dietPlanRepository.findDietPlansByUserId(user.getId());

        // then
        assertThat(plans).hasSize(2)
                .extracting(DietPlan::getId)
                .containsExactlyInAnyOrder(dietPlan1.getId(), dietPlan2.getId());
    }

    @DisplayName("pk 기반으로 해당 diet plan 이 존재하는지 확인할 수 있다.")
    @Test
    void existsById() {
        // given
        User user = createUser("test user", "test nickname", "test@email.com", "password");
        userRepository.save(user);

        DietPlan dietPlan = createDietPlan(user.getId(), "title", "content", false, true, LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan);

        // when
        boolean existsResult = dietPlanRepository.existsById(dietPlan.getId());
        boolean notExistsResult = dietPlanRepository.existsById(1000L);

        // then
        assertThat(existsResult).isTrue();
        assertThat(notExistsResult).isFalse();
    }

    @DisplayName("pk 기반으로 단건 삭제할 수 있다.")
    @Test
    void deleteById() {
        // given
        User user = createUser("test user", "test nickname", "test@email.com", "password");
        userRepository.save(user);

        DietPlan dietPlan = createDietPlan(user.getId(), "title", "content", false, true, LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan);

        // when
        dietPlanRepository.deleteById(dietPlan.getId());
        Optional<DietPlan> findDietPlanOpt = dietPlanRepository.findById(dietPlan.getId());

        // then
        assertThat(findDietPlanOpt).isEmpty();
    }

    @DisplayName("사용자의 대표 식단을 조회할 수 있다.")
    @Test
    void findUsersPrimaryDietPlan() {
        // given
        User user = createUser("test user", "test nickname", "test@email.com", "password");
        userRepository.save(user);

        DietPlan dietPlan1 = createDietPlan(user.getId(), "title", "content", false, false, LocalDate.now(), LocalDate.now().plusDays(1));
        DietPlan dietPlan2 = createDietPlan(user.getId(), "title", "content2", false, true, LocalDate.now(), LocalDate.now().plusDays(1));

        dietPlanRepository.insert(dietPlan1);
        dietPlanRepository.insert(dietPlan2);

        // when
        Optional<DietPlan> primaryDietPlanOpt = dietPlanRepository.findUsersPrimaryDietPlan(user.getId());

        //then
        assertThat(primaryDietPlanOpt).isPresent();

        DietPlan primaryDietPlan = primaryDietPlanOpt.get();
        assertDietPlanEquals(primaryDietPlan, dietPlan2);
    }

    @DisplayName("사용자의 대표 식단을 비활성화 할 수 있다.")
    @Test
    void deActivateCurrentPrimaryDietPlan() {
        // given
        User user = createUser("test user", "test nickname", "test@email.com", "password");
        userRepository.save(user);

        DietPlan dietPlan = createDietPlan(user.getId(), "title", "content", false, true, LocalDate.now(), LocalDate.now().plusDays(1));

        dietPlanRepository.insert(dietPlan);

        // when
        dietPlanRepository.deActivateCurrentPrimaryDietPlan(user.getId());
        Optional<DietPlan> usersPrimaryDietPlanOpt = dietPlanRepository.findUsersPrimaryDietPlan(user.getId());

        //then
        assertThat(usersPrimaryDietPlanOpt).isEmpty();
    }

    @DisplayName("특정 식단을 사용자의 대표식단으로 지정할 수 있다.")
    @Test
    void activateCurrentPrimaryDietPlan() {
        // given
        User user = createUser("test user", "test nickname", "test@email.com", "password");
        userRepository.save(user);

        DietPlan dietPlan = createDietPlan(user.getId(), "title", "content", false, false, LocalDate.now(), LocalDate.now().plusDays(1));

        dietPlanRepository.insert(dietPlan);

        // when
        dietPlanRepository.activateCurrentPrimaryDietPlan(user.getId(), dietPlan.getId());
        Optional<DietPlan> usersPrimaryDietPlanOpt = dietPlanRepository.findUsersPrimaryDietPlan(user.getId());

        //then
        assertThat(usersPrimaryDietPlanOpt).isPresent();
        DietPlan primaryDietPlan = usersPrimaryDietPlanOpt.get();
        assertThat(primaryDietPlan.isPrimary()).isTrue();
    }

    @DisplayName("식단 계획 정보를 업데이트할 수 있다.")
    @Nested
    class UpdateTest {

        @DisplayName("Content 필드만 업데이트 할 수 있어야 한다.")
        @Test
        void updateOnlyContent() {
            // given
            User user = createUser("test user", "test nickname", "test@email.com", "password");
            userRepository.save(user);

            // 기존 데이터 생성 (시작일/종료일 기준)
            LocalDate oldStartDate = LocalDate.of(2025, 1, 1);
            LocalDate oldEndDate = LocalDate.of(2025, 1, 10);

            DietPlan originalPlan = createDietPlan(user.getId(), "Original Title", "Old Content", false, false, oldStartDate, oldEndDate);
            dietPlanRepository.insert(originalPlan);

            // 업데이트 요청 데이터 (Content만 변경)
            String newContent = "New Content Updated";
            UpdateDietPlanRepositoryRequest request = new UpdateDietPlanRepositoryRequest();
            request.setDietPlanId(originalPlan.getId());
            request.setContent(newContent);

            // when
            int updatedRows = dietPlanRepository.update(request);
            Optional<DietPlan> updatedPlanOpt = dietPlanRepository.findById(originalPlan.getId());

            // then
            assertThat(updatedRows).isEqualTo(1);
            assertThat(updatedPlanOpt).isPresent();

            DietPlan updatedPlan = updatedPlanOpt.get();

            assertThat(updatedPlan.getContent()).isEqualTo(newContent);
            assertThat(updatedPlan.getStartDate()).isEqualTo(oldStartDate);
            assertThat(updatedPlan.getEndDate()).isEqualTo(oldEndDate);
        }

        @DisplayName("StartDate 필드만 업데이트 할 수 있어야 한다.")
        @Test
        void updateOnlyStartDate() {
            // given
            User user = createUser("test user", "test nickname", "test@email.com", "password");
            userRepository.save(user);

            LocalDate oldStartDate = LocalDate.of(2025, 1, 1);
            LocalDate oldEndDate = LocalDate.of(2025, 1, 10);

            DietPlan originalPlan = createDietPlan(user.getId(), "Title", "Content", false, false, oldStartDate, oldEndDate);
            dietPlanRepository.insert(originalPlan);

            // 업데이트 요청 데이터 (StartDate만 변경)
            LocalDate newStartDate = LocalDate.of(2025, 1, 5);
            UpdateDietPlanRepositoryRequest request = new UpdateDietPlanRepositoryRequest();
            request.setDietPlanId(originalPlan.getId());
            request.setStartDate(newStartDate);
            // content와 endDate는 null 유지

            // when
            int updatedRows = dietPlanRepository.update(request);
            Optional<DietPlan> updatedPlanOpt = dietPlanRepository.findById(originalPlan.getId());

            // then
            assertThat(updatedRows).isEqualTo(1);
            assertThat(updatedPlanOpt).isPresent();

            DietPlan updatedPlan = updatedPlanOpt.get();

            assertThat(updatedPlan.getStartDate()).isEqualTo(newStartDate);
            assertThat(updatedPlan.getContent()).isEqualTo(originalPlan.getContent());
            assertThat(updatedPlan.getEndDate()).isEqualTo(oldEndDate);
        }

        @DisplayName("EndDate 필드만 업데이트 할 수 있어야 한다.")
        @Test
        void updateOnlyEndDate() {
            // given
            User user = createUser("test user", "test nickname", "test@email.com", "password");
            userRepository.save(user);

            LocalDate oldStartDate = LocalDate.of(2025, 1, 1);
            LocalDate oldEndDate = LocalDate.of(2025, 1, 10);

            DietPlan originalPlan = createDietPlan(user.getId(), "Title", "Content", false, false, oldStartDate, oldEndDate);
            dietPlanRepository.insert(originalPlan);

            // 업데이트 요청 데이터 (EndDate만 변경)
            LocalDate newEndDate = LocalDate.of(2025, 1, 31);
            UpdateDietPlanRepositoryRequest request = new UpdateDietPlanRepositoryRequest();
            request.setDietPlanId(originalPlan.getId());
            request.setEndDate(newEndDate);
            // content와 startDate는 null 유지

            // when
            int updatedRows = dietPlanRepository.update(request);
            Optional<DietPlan> updatedPlanOpt = dietPlanRepository.findById(originalPlan.getId());

            // then
            assertThat(updatedRows).isEqualTo(1);
            assertThat(updatedPlanOpt).isPresent();

            DietPlan updatedPlan = updatedPlanOpt.get();

            assertThat(updatedPlan.getEndDate()).isEqualTo(newEndDate);
            assertThat(updatedPlan.getContent()).isEqualTo(originalPlan.getContent());
            assertThat(updatedPlan.getStartDate()).isEqualTo(oldStartDate);
        }

        // --- 다중 필드 업데이트 시나리오 ---

        @DisplayName("Content와 Date 필드를 동시에 업데이트 할 수 있어야 한다.")
        @Test
        void updateMultipleFields() {
            // given
            User user = createUser("test user", "test nickname", "test@email.com", "password");
            userRepository.save(user);

            DietPlan originalPlan = createDietPlan(user.getId(), "Old Content", "Title", false, false, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 10));
            dietPlanRepository.insert(originalPlan);

            // 업데이트 요청 데이터 (Content, StartDate, EndDate 모두 변경)
            String newContent = "All Fields Changed";
            LocalDate newStartDate = LocalDate.of(2025, 2, 1);
            LocalDate newEndDate = LocalDate.of(2025, 2, 28);

            UpdateDietPlanRepositoryRequest request = new UpdateDietPlanRepositoryRequest();
            request.setDietPlanId(originalPlan.getId());
            request.setContent(newContent);
            request.setStartDate(newStartDate);
            request.setEndDate(newEndDate);

            // when
            int updatedRows = dietPlanRepository.update(request);
            Optional<DietPlan> updatedPlanOpt = dietPlanRepository.findById(originalPlan.getId());

            // then
            assertThat(updatedRows).isEqualTo(1);
            assertThat(updatedPlanOpt).isPresent();

            DietPlan updatedPlan = updatedPlanOpt.get();

            // 모든 필드가 변경되었는지 확인
            assertThat(updatedPlan.getContent()).isEqualTo(newContent);
            assertThat(updatedPlan.getStartDate()).isEqualTo(newStartDate);
            assertThat(updatedPlan.getEndDate()).isEqualTo(newEndDate);
        }
    }
}
