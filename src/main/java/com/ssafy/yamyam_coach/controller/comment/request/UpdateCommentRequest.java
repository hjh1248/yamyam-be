package com.ssafy.yamyam_coach.controller.comment.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCommentRequest {

    @NotBlank(message = "content 는 비거나 null 일 수 없습니다.")
    private String newContent;

}
