package com.ssafy.yamyam_coach.repository.comment;

import com.ssafy.yamyam_coach.IntegrationTestSupport;
import com.ssafy.yamyam_coach.domain.comment.Comment;
import com.ssafy.yamyam_coach.domain.dietplan.DietPlan;
import com.ssafy.yamyam_coach.domain.post.Post;
import com.ssafy.yamyam_coach.domain.user.User;
import com.ssafy.yamyam_coach.repository.diet_plan.DietPlanRepository;
import com.ssafy.yamyam_coach.repository.post.PostRepository;
import com.ssafy.yamyam_coach.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.ssafy.yamyam_coach.util.DomainAssertions.assertCommentEquals;
import static com.ssafy.yamyam_coach.util.TestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class CommentRepositoryTest extends IntegrationTestSupport {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DietPlanRepository dietPlanRepository;

    @Autowired
    PostRepository postRepository;

    @Nested
    @DisplayName("insert")
    class Insert {

        @DisplayName("comment를 저장할 수 있다.")
        @Test
        void insert() {
            // given
            User user = createDummyUser();
            userRepository.save(user);

            DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
            dietPlanRepository.insert(dietPlan);

            Post post = createDummyPost(user.getId(), dietPlan.getId());
            postRepository.insert(post);

            Comment comment = createComment(user.getId(), post.getId(), "test");

            //when
            commentRepository.insert(comment);
            Comment findComment = commentRepository.findById(comment.getId()).orElse(null);

            //then
            assertCommentEquals(findComment, comment);
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @DisplayName("ID로 comment를 조회할 수 있다.")
        @Test
        void findById() {
            // given
            User user = createDummyUser();
            userRepository.save(user);

            DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
            dietPlanRepository.insert(dietPlan);

            Post post = createDummyPost(user.getId(), dietPlan.getId());
            postRepository.insert(post);

            Comment comment = createComment(user.getId(), post.getId(), "test");
            commentRepository.insert(comment);

            //when
            Comment findComment = commentRepository.findById(comment.getId()).orElse(null);

            //then
            assertThat(findComment).isNotNull();
            assertThat(findComment.getId()).isEqualTo(comment.getId());
            assertThat(findComment.getContent()).isEqualTo("test");
            assertThat(findComment.getUserId()).isEqualTo(user.getId());
            assertThat(findComment.getPostId()).isEqualTo(post.getId());
        }

        @DisplayName("존재하지 않는 ID로 조회 시 empty Optional을 반환한다.")
        @Test
        void findByIdNotFound() {
            // given
            Long notExistingCommentId = 99999L;

            //when
            Comment findComment = commentRepository.findById(notExistingCommentId).orElse(null);

            //then
            assertThat(findComment).isNull();
        }
    }

    @Nested
    @DisplayName("findByPostId")
    class FindByPostId {

        @DisplayName("post와 관련된 모든 comment를 조회할 수 있다.")
        @Test
        void findByPostId() {
            // given
            User user = createDummyUser();
            userRepository.save(user);

            DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
            dietPlanRepository.insert(dietPlan);

            Post post = createDummyPost(user.getId(), dietPlan.getId());
            postRepository.insert(post);

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime next = now.plusDays(1);
            LocalDateTime end = next.plusDays(1);

            Comment comment1 = createComment(user.getId(), post.getId(), "test1", now);
            commentRepository.insert(comment1);

            Comment comment2 = createComment(user.getId(), post.getId(), "test2", next);
            commentRepository.insert(comment2);

            Comment comment3 = createComment(user.getId(), post.getId(), "test3", end);
            commentRepository.insert(comment3);

            //when
            List<Comment> comments = commentRepository.findByPostId(post.getId());

            //then
            assertThat(comments).hasSize(3)
                    .extracting(Comment::getId)
                    .containsExactly(comment3.getId(), comment2.getId(), comment1.getId());
        }

        @DisplayName("댓글이 없는 post 조회 시 빈 리스트를 반환한다.")
        @Test
        void findByPostIdEmpty() {
            // given
            User user = createDummyUser();
            userRepository.save(user);

            DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
            dietPlanRepository.insert(dietPlan);

            Post post = createDummyPost(user.getId(), dietPlan.getId());
            postRepository.insert(post);

            //when
            List<Comment> comments = commentRepository.findByPostId(post.getId());

            //then
            assertThat(comments).isEmpty();
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @DisplayName("comment의 content를 수정할 수 있다.")
        @Test
        void update() {
            // given
            User user = createDummyUser();
            userRepository.save(user);

            DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
            dietPlanRepository.insert(dietPlan);

            Post post = createDummyPost(user.getId(), dietPlan.getId());
            postRepository.insert(post);

            Comment comment = createComment(user.getId(), post.getId(), "test");
            commentRepository.insert(comment);

            String newContent = "new content";

            //when
            int updateCount = commentRepository.update(comment.getId(), newContent);
            Comment findComment = commentRepository.findById(comment.getId()).orElse(null);

            //then
            assertThat(updateCount).isEqualTo(1);
            assertThat(findComment).isNotNull();
            assertThat(findComment.getContent()).isEqualTo(newContent);
            assertThat(findComment.getUserId()).isEqualTo(user.getId());
            assertThat(findComment.getPostId()).isEqualTo(post.getId());
        }
    }

    @Nested
    @DisplayName("deleteById")
    class DeleteById {

        @DisplayName("comment를 삭제할 수 있다.")
        @Test
        void deleteById() {
            // given
            User user = createDummyUser();
            userRepository.save(user);

            DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
            dietPlanRepository.insert(dietPlan);

            Post post = createDummyPost(user.getId(), dietPlan.getId());
            postRepository.insert(post);

            Comment comment = createComment(user.getId(), post.getId(), "test");
            commentRepository.insert(comment);

            // when
            int deleteCount = commentRepository.deleteById(comment.getId());
            Comment findComment = commentRepository.findById(comment.getId()).orElse(null);

            //then
            assertThat(deleteCount).isEqualTo(1);
            assertThat(findComment).isNull();
        }

        @DisplayName("존재하지 않는 comment 삭제 시 0을 반환한다.")
        @Test
        void deleteByIdNotFound() {
            // given
            Long notExistingCommentId = 99999L;

            // when
            int deleteCount = commentRepository.deleteById(notExistingCommentId);

            //then
            assertThat(deleteCount).isEqualTo(0);
        }
    }
}
