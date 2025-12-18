package com.ssafy.yamyam_coach.repository.post.response;

import com.ssafy.yamyam_coach.domain.dietplan.DietPlan;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDetailResponse {

    private Long postId;
    private String title;
    private String content;
    private Integer likeCount;
    private Boolean isLiked;
    private LocalDateTime createdAt;

    private UserDetailResponse author;
    private DietPlanDetailResponse dietPlan;
    private List<CommentDetailResponse> comments;
}
