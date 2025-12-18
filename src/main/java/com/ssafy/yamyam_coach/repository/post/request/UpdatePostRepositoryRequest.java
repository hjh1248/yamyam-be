package com.ssafy.yamyam_coach.repository.post.request;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdatePostRepositoryRequest {

    private Long postId;
    private Long dietPlanId;
    private String title;
    private String content;

    @Builder
    private UpdatePostRepositoryRequest(Long postId, Long dietPlanId, String title, String content) {
        this.postId = postId;
        this.dietPlanId = dietPlanId;
        this.title = title;
        this.content = content;
    }
}
