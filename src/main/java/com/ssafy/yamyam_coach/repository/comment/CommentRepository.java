package com.ssafy.yamyam_coach.repository.comment;

import com.ssafy.yamyam_coach.domain.comment.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    int insert(Comment comment);

    Optional<Comment> findById(Long commentId);

    List<Comment> findByPostId(Long postId);

    int update(Long commentId, String content);

    int deleteById(Long commentId);
}
