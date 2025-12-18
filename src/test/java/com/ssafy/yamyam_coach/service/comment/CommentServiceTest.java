package com.ssafy.yamyam_coach.service.comment;

import com.ssafy.yamyam_coach.IntegrationTestSupport;
import com.ssafy.yamyam_coach.domain.comment.Comment;
import com.ssafy.yamyam_coach.domain.post.Post;
import com.ssafy.yamyam_coach.domain.user.User;
import com.ssafy.yamyam_coach.exception.comment.CommentException;
import com.ssafy.yamyam_coach.exception.post.PostException;
import com.ssafy.yamyam_coach.repository.comment.CommentRepository;
import com.ssafy.yamyam_coach.repository.post.PostRepository;
import com.ssafy.yamyam_coach.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ssafy.yamyam_coach.util.TestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentServiceTest extends IntegrationTestSupport {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Nested
    @DisplayName("registerComment")
    class RegisterComment {

        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase {

            @DisplayName("댓글을 정상적으로 등록한다.")
            @Test
            void registerComment() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                Post post = createDummyPost(user.getId(), null);
                postRepository.insert(post);

                String content = "댓글 내용";

                // when
                Long commentId = commentService.registerComment(user.getId(), post.getId(), content);

                // then
                Comment findComment = commentRepository.findById(commentId).orElse(null);
                assertThat(findComment).isNotNull();
                assertThat(findComment.getContent()).isEqualTo(content);
                assertThat(findComment.getUserId()).isEqualTo(user.getId());
                assertThat(findComment.getPostId()).isEqualTo(post.getId());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailureCase {

            @DisplayName("존재하지 않는 게시글에 댓글 등록 시 NOT_FOUND_POST 예외가 발생한다.")
            @Test
            void registerCommentToNotExistingPost() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                Long notExistingPostId = 99999L;
                String content = "댓글 내용";

                // when // then
                assertThatThrownBy(() -> commentService.registerComment(user.getId(), notExistingPostId, content))
                        .isInstanceOf(PostException.class)
                        .hasMessage("해당 게시글을 찾을 수 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("updateComment")
    class UpdateComment {

        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase {

            @DisplayName("댓글 내용을 정상적으로 수정한다.")
            @Test
            void updateComment() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                Post post = createDummyPost(user.getId(), null);
                postRepository.insert(post);

                Comment comment = createComment(user.getId(), post.getId(), "원래 내용");
                commentRepository.insert(comment);

                String newContent = "수정된 내용";

                // when
                commentService.updateComment(user.getId(), post.getId(), comment.getId(), newContent);

                // then
                Comment updatedComment = commentRepository.findById(comment.getId()).orElse(null);
                assertThat(updatedComment).isNotNull();
                assertThat(updatedComment.getContent()).isEqualTo(newContent);
            }

            @DisplayName("같은 내용으로 수정 시 변경이 발생하지 않는다.")
            @Test
            void updateCommentWithSameContent() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                Post post = createDummyPost(user.getId(), null);
                postRepository.insert(post);

                String originalContent = "원래 내용";
                Comment comment = createComment(user.getId(), post.getId(), originalContent);
                commentRepository.insert(comment);

                // when
                commentService.updateComment(user.getId(), post.getId(), comment.getId(), originalContent);

                // then
                Comment updatedComment = commentRepository.findById(comment.getId()).orElse(null);
                assertThat(updatedComment).isNotNull();
                assertThat(updatedComment.getContent()).isEqualTo(originalContent);
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailureCase {

            @DisplayName("존재하지 않는 게시글의 댓글 수정 시 NOT_FOUND_POST 예외가 발생한다.")
            @Test
            void updateCommentOfNotExistingPost() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                Post post = createDummyPost(user.getId(), null);
                postRepository.insert(post);

                Comment comment = createComment(user.getId(), post.getId(), "원래 내용");
                commentRepository.insert(comment);

                Long notExistingPostId = 99999L;
                String newContent = "수정된 내용";

                // when // then
                assertThatThrownBy(() -> commentService.updateComment(user.getId(), notExistingPostId, comment.getId(), newContent))
                        .isInstanceOf(PostException.class)
                        .hasMessage("해당 게시글을 찾을 수 없습니다.");
            }

            @DisplayName("존재하지 않는 댓글 수정 시 NOT_FOUND_COMMENT 예외가 발생한다.")
            @Test
            void updateNotExistingComment() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                Post post = createDummyPost(user.getId(), null);
                postRepository.insert(post);

                Long notExistingCommentId = 99999L;
                String newContent = "수정된 내용";

                // when // then
                assertThatThrownBy(() -> commentService.updateComment(user.getId(), post.getId(), notExistingCommentId, newContent))
                        .isInstanceOf(CommentException.class)
                        .hasMessage("해당 댓글을 찾을 수 없습니다.");
            }

            @DisplayName("다른 게시글의 댓글 수정 시 COMMENT_NOT_FOUND_IN_POST 예외가 발생한다.")
            @Test
            void updateCommentOfDifferentPost() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                Post post1 = createDummyPost(user.getId(), null);
                postRepository.insert(post1);

                Post post2 = createDummyPost(user.getId(), null);
                postRepository.insert(post2);

                Comment comment = createComment(user.getId(), post1.getId(), "원래 내용");
                commentRepository.insert(comment);

                String newContent = "수정된 내용";

                // when // then
                assertThatThrownBy(() -> commentService.updateComment(user.getId(), post2.getId(), comment.getId(), newContent))
                        .isInstanceOf(CommentException.class)
                        .hasMessage("해당 게시글에 해당 댓글을 조회할 수 없습니다.");
            }

            @DisplayName("다른 사용자의 댓글 수정 시 ACCESS_DENIED_COMMENT 예외가 발생한다.")
            @Test
            void updateOtherUsersComment() {
                // given
                User user1 = createDummyUser();
                userRepository.save(user1);

                User user2 = createUser("다른사람", "다른닉네임", "other@test.com", "password");
                userRepository.save(user2);

                Post post = createDummyPost(user1.getId(), null);
                postRepository.insert(post);

                Comment comment = createComment(user1.getId(), post.getId(), "원래 내용");
                commentRepository.insert(comment);

                String newContent = "수정된 내용";

                // when // then
                assertThatThrownBy(() -> commentService.updateComment(user2.getId(), post.getId(), comment.getId(), newContent))
                        .isInstanceOf(CommentException.class)
                        .hasMessage("해당 댓글에 대한 작업 권한이 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("deleteComment")
    class DeleteComment {

        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase {

            @DisplayName("댓글을 정상적으로 삭제한다.")
            @Test
            void deleteComment() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                Post post = createDummyPost(user.getId(), null);
                postRepository.insert(post);

                Comment comment = createComment(user.getId(), post.getId(), "댓글 내용");
                commentRepository.insert(comment);

                // when
                commentService.deleteComment(user.getId(), post.getId(), comment.getId());

                // then
                Comment deletedComment = commentRepository.findById(comment.getId()).orElse(null);
                assertThat(deletedComment).isNull();
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailureCase {

            @DisplayName("존재하지 않는 게시글의 댓글 삭제 시 NOT_FOUND_POST 예외가 발생한다.")
            @Test
            void deleteCommentOfNotExistingPost() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                Post post = createDummyPost(user.getId(), null);
                postRepository.insert(post);

                Comment comment = createComment(user.getId(), post.getId(), "댓글 내용");
                commentRepository.insert(comment);

                Long notExistingPostId = 99999L;

                // when // then
                assertThatThrownBy(() -> commentService.deleteComment(user.getId(), notExistingPostId, comment.getId()))
                        .isInstanceOf(PostException.class)
                        .hasMessage("해당 게시글을 찾을 수 없습니다.");
            }

            @DisplayName("존재하지 않는 댓글 삭제 시 NOT_FOUND_COMMENT 예외가 발생한다.")
            @Test
            void deleteNotExistingComment() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                Post post = createDummyPost(user.getId(), null);
                postRepository.insert(post);

                Long notExistingCommentId = 99999L;

                // when // then
                assertThatThrownBy(() -> commentService.deleteComment(user.getId(), post.getId(), notExistingCommentId))
                        .isInstanceOf(CommentException.class)
                        .hasMessage("해당 댓글을 찾을 수 없습니다.");
            }

            @DisplayName("다른 게시글의 댓글 삭제 시 COMMENT_NOT_FOUND_IN_POST 예외가 발생한다.")
            @Test
            void deleteCommentOfDifferentPost() {
                // given
                User user = createDummyUser();
                userRepository.save(user);

                Post post1 = createDummyPost(user.getId(), null);
                postRepository.insert(post1);

                Post post2 = createDummyPost(user.getId(), null);
                postRepository.insert(post2);

                Comment comment = createComment(user.getId(), post1.getId(), "댓글 내용");
                commentRepository.insert(comment);

                // when // then
                assertThatThrownBy(() -> commentService.deleteComment(user.getId(), post2.getId(), comment.getId()))
                        .isInstanceOf(CommentException.class)
                        .hasMessage("해당 게시글에 해당 댓글을 조회할 수 없습니다.");
            }

            @DisplayName("다른 사용자의 댓글 삭제 시 ACCESS_DENIED_COMMENT 예외가 발생한다.")
            @Test
            void deleteOtherUsersComment() {
                // given
                User user1 = createDummyUser();
                userRepository.save(user1);

                User user2 = createUser("다른사람", "다른닉네임", "other@test.com", "password");
                userRepository.save(user2);

                Post post = createDummyPost(user1.getId(), null);
                postRepository.insert(post);

                Comment comment = createComment(user1.getId(), post.getId(), "댓글 내용");
                commentRepository.insert(comment);

                // when // then
                assertThatThrownBy(() -> commentService.deleteComment(user2.getId(), post.getId(), comment.getId()))
                        .isInstanceOf(CommentException.class)
                        .hasMessage("해당 댓글에 대한 작업 권한이 없습니다.");
            }
        }
    }
}
