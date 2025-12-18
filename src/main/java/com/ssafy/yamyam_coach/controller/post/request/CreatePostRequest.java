package com.ssafy.yamyam_coach.controller.post.request;

import com.ssafy.yamyam_coach.service.post.request.CreatePostServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePostRequest {

    private Long dietPlanId;

    @NotBlank(message = "제목을 반드시 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 반드시 입력해 주세요")
    private String content;

    public CreatePostServiceRequest toServiceRequest() {
        return CreatePostServiceRequest.builder()
                .dietPlanId(dietPlanId)
                .title(title)
                .content(content)
                .build();
    }
}
