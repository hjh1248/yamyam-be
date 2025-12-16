package com.ssafy.yamyam_coach.repository.comment;

import com.ssafy.yamyam_coach.domain.comment.Comment;
import com.ssafy.yamyam_coach.mapper.comment.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisCommentRepository implements CommentRepository {

    private final CommentMapper commentMapper;

    @Override
    public int insert(Comment comment) {
        return commentMapper.insert(comment);
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return Optional.ofNullable(commentMapper.findById(commentId));
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return commentMapper.findByPostId(postId);
    }

    @Override
    public int update(Long commentId, String content) {
        return commentMapper.update(commentId, content);
    }

    @Override
    public int deleteById(Long commentId) {
        return commentMapper.deleteById(commentId);
    }
}
