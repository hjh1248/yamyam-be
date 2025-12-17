package com.ssafy.yamyam_coach.service.body_spec.response;

import com.ssafy.yamyam_coach.domain.body_spec.BodySpec;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
public class BodySpecServiceResponse {
    private Long id;
    private int height;
    private int weight;
    private int age;
    private String gender;
    private LocalDate date; // 프론트는 날짜만 필요하니까 LocalDate로 변환

    @Builder
    private BodySpecServiceResponse(Long id, int height, int weight, int age, String gender, LocalDate date) {
        this.id = id;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.date = date;
    }

    public static BodySpecServiceResponse of(BodySpec bodySpec) {
        return BodySpecServiceResponse.builder()
                .id(bodySpec.getId())
                .height(bodySpec.getHeight())
                .weight(bodySpec.getWeight())
                .age(bodySpec.getAge())
                .gender(bodySpec.getGender())
                // created_at(LocalDateTime)을 프론트용 date(LocalDate)로 변환
                .date(bodySpec.getCreatedAt().toLocalDate())
                .build();
    }
}