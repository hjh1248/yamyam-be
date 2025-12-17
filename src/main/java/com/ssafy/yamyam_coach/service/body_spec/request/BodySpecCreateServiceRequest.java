package com.ssafy.yamyam_coach.service.body_spec.request;

import com.ssafy.yamyam_coach.domain.body_spec.BodySpec;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
public class BodySpecCreateServiceRequest {
    private int height;
    private int weight;
    private int age;
    private String gender;
    private LocalDate createdAt;

    @Builder
    private BodySpecCreateServiceRequest(int height, int weight, int age, String gender, LocalDate createdAt) {
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.createdAt = createdAt;
    }

    public BodySpec toEntity(Long userId) {

        return BodySpec.builder()
                .userId(userId)
                .height(this.height)
                .weight(this.weight)
                .age(this.age)
                .gender(this.gender)
                .createdAt(this.createdAt.atStartOfDay())
                .build();
    }
}