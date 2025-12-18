package com.ssafy.yamyam_coach.repository.post.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDetailResponse {

    private Long commentId;
    private String content;
    private LocalDateTime createdAt;

    private UserDetailResponse author;

}
