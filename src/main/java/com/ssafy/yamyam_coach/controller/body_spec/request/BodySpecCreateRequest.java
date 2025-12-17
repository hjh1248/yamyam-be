package com.ssafy.yamyam_coach.controller.body_spec.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.yamyam_coach.service.body_spec.request.BodySpecCreateServiceRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BodySpecCreateRequest {
    private int height;
    private int weight;
    private int age;
    private String gender;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    public BodySpecCreateServiceRequest toServiceDto() {
        return BodySpecCreateServiceRequest.builder()
                .height(this.height)
                .weight(this.weight)
                .age(this.age)
                .gender(this.gender)
                .createdAt(this.createdAt) // 받은 날짜를 그대로 넘김
                .build();
    }
}