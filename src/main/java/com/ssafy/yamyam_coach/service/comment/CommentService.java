package com.ssafy.yamyam_coach.service.comment;

import com.ssafy.yamyam_coach.domain.comment.Comment;
import com.ssafy.yamyam_coach.exception.comment.CommentErrorCode;
import com.ssafy.yamyam_coach.exception.comment.CommentException;
import com.ssafy.yamyam_coach.exception.post.PostException;
import com.ssafy.yamyam_coach.repository.comment.CommentRepository;
import com.ssafy.yamyam_coach.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.ssafy.yamyam_coach.exception.comment.CommentErrorCode.COMMENT_NOT_FOUND_IN_POST;
import static com.ssafy.yamyam_coach.exception.post.PostErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long registerComment(Long currentUserId, Long postId, String content) {
        // 1. post 조회 및 존재 검증
        postRepository.findById(postId)
                .orElseThrow(() -> new PostException(NOT_FOUND_POST));

        // 2. comment 생성
        Comment comment = createComment(currentUserId, postId, content);

        // 3. comment 저장
        commentRepository.insert(comment);

        return comment.getId();
    }

    @Transactional
    public void updateComment(Long currentUserId, Long postId, Long commentId, String newContent) {
        // 1. post 존재 검증
        postRepository.findById(postId)
                .orElseThrow(() -> new PostException(NOT_FOUND_POST));

        // 2. comment 조회 및 존재 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.NOT_FOUND_COMMENT));

        // 3. comment 가 해당 post 의 comment 인지 검증
        validateCommentBelongsToPost(postId, comment.getPostId());

        // 4. comment 의 작성자가 현재 요청자인지 검증
        validateUser(currentUserId, comment.getUserId());

        // 5. 기존 내용과 동일하다면 return
        if (comment.getContent().equals(newContent)) {
            return ;
        }

        // 6. 새로운 content 로 comment 의 내용을 변경
        commentRepository.update(commentId, newContent);
    }

    @Transactional
    public void deleteComment(Long currentUserId, Long postId, Long commentId) {
        // 1. post 존재 검증
        postRepository.findById(postId)
                .orElseThrow(() -> new PostException(NOT_FOUND_POST));

        // 2. comment 조회 및 존재 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.NOT_FOUND_COMMENT));

        // 3. comment 가 해당 post 의 comment 인지 검증
        validateCommentBelongsToPost(postId, comment.getPostId());

        // 4. comment 의 작성자가 현재 요청자인지 검증
        validateUser(currentUserId, comment.getUserId());

        // 5. 삭제
        commentRepository.deleteById(commentId);
    }

    private void validateUser(Long currentUserId, Long commentsUserId) {
        if (!currentUserId.equals(commentsUserId)) {
            throw new CommentException(CommentErrorCode.ACCESS_DENIED_COMMENT);
        }
    }

    private void validateCommentBelongsToPost(Long postId, Long commentsPostId) {
        if (!postId.equals(commentsPostId)) {
            throw new CommentException(COMMENT_NOT_FOUND_IN_POST);
        }
    }

    private Comment createComment(Long currentUserId, Long postId, String content) {
        return Comment.builder()
                .content(content)
                .userId(currentUserId)
                .postId(postId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
