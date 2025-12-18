package com.ssafy.yamyam_coach.controller.comment;

import com.ssafy.yamyam_coach.controller.comment.request.CreateCommentRequest;
import com.ssafy.yamyam_coach.controller.comment.request.UpdateCommentRequest;
import com.ssafy.yamyam_coach.domain.user.User;
import com.ssafy.yamyam_coach.global.annotation.LoginUser;
import com.ssafy.yamyam_coach.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> registerComment(@LoginUser User currentUser, @PathVariable Long postId, @RequestBody @Valid CreateCommentRequest request) {
        commentService.registerComment(currentUser.getId(), postId, request.getContent());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@LoginUser User currentUser, @PathVariable Long postId, @PathVariable Long commentId, @RequestBody @Valid UpdateCommentRequest request) {
        commentService.updateComment(currentUser.getId(), postId, commentId, request.getNewContent());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@LoginUser User currentUser, @PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(currentUser.getId(), postId, commentId);
        return ResponseEntity.ok().build();
    }

}
