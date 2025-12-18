package com.ssafy.yamyam_coach.service.post.request;

import lombok.Builder;
import lombok.Data;

@Data
public class CreatePostServiceRequest {

    private Long dietPlanId;
    private String title;
    private String content;

    @Builder
    private CreatePostServiceRequest(Long dietPlanId, String title, String content) {
        this.dietPlanId = dietPlanId;
        this.title = title;
        this.content = content;
    }
}
