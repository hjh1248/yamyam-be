package com.ssafy.yamyam_coach.mapper.comment;

import com.ssafy.yamyam_coach.domain.comment.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper{
    int insert(Comment comment);

    Comment findById(Long commentId);

    List<Comment> findByPostId(Long postId);

    int update(@Param("commentId") Long commentId, @Param("content") String content);

    int deleteById(Long commentId);
}
