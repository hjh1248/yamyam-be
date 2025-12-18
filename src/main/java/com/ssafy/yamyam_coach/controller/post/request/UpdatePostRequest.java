package com.ssafy.yamyam_coach.controller.post.request;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdatePostRequest {

    private Long dietPlanId;
    private String title;
    private String content;

    @Builder
    private UpdatePostRequest(Long dietPlanId, String title, String content) {
        this.dietPlanId = dietPlanId;
        this.title = title;
        this.content = content;
    }
}
