package com.ssafy.yamyam_coach.controller.food;

import com.ssafy.yamyam_coach.MockLoginUserArgumentResolver;
import com.ssafy.yamyam_coach.RestControllerTestSupport;
import com.ssafy.yamyam_coach.domain.food.BaseUnit;
import com.ssafy.yamyam_coach.exception.common.advice.GlobalRestExceptionHandler;
import com.ssafy.yamyam_coach.service.food.FoodService;
import com.ssafy.yamyam_coach.service.food.response.SearchFoodServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FoodController.class)
@AutoConfigureMockMvc(addFilters = false)
class FoodControllerTest extends RestControllerTestSupport {

    @MockitoBean
    FoodService foodService;

    @BeforeEach
    void setUp() {
        MockLoginUserArgumentResolver mockLoginUserArgumentResolver = new MockLoginUserArgumentResolver(mockUser);

        mockMvc = MockMvcBuilders.standaloneSetup(new FoodController(foodService))
                .setCustomArgumentResolvers(mockLoginUserArgumentResolver)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("searchFood")
    class SearchFood {

        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase {

            @DisplayName("요청된 단어를 포함하는 이름을 가지는 음식들이 200 응답과 함께 반환된다.")
            @Test
            void searchFood() throws Exception {
                // given
                SearchFoodServiceResponse response = SearchFoodServiceResponse.builder()
                        .foodId(1L)
                        .name("food")
                        .category("category")
                        .baseUnit(BaseUnit.g)
                        .caloriePer100(100.0)
                        .build();

                // when
                given(foodService.searchFood(anyString()))
                        .willReturn(List.of(response));

                // then
                mockMvc.perform(
                                get("/api/foods/search")
                                        .param("name", "test")
                        )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].foodId").value(1))
                        .andExpect(jsonPath("$[0].name").value("food"))
                        .andExpect(jsonPath("$[0].category").value("category"))
                        .andExpect(jsonPath("$[0].baseUnit").value(BaseUnit.g.name()))
                        .andExpect(jsonPath("$[0].caloriePer100").value(100.0));

            }

            @DisplayName("요청된 단어를 포함하는 이름을 가지는 음식들이 없으면 200 응답과 함께 빈 리스트가 반환된다.")
            @Test
            void notExistsName() throws Exception {
                // given
                // when
                given(foodService.searchFood(anyString()))
                        .willReturn(List.of());

                // then
                mockMvc.perform(
                                get("/api/foods/search")
                                        .param("name", "test")
                        )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray());

            }
        }

    }
}