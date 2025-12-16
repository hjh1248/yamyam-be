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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.ssafy.yamyam_coach.util.DomainAssertions.assertCommentEquals;
import static com.ssafy.yamyam_coach.util.TestFixtures.*;
import static com.ssafy.yamyam_coach.util.TestFixtures.createDummyDietPlan;
import static com.ssafy.yamyam_coach.util.TestFixtures.createDummyPost;
import static com.ssafy.yamyam_coach.util.TestFixtures.createDummyUser;
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

    @DisplayName("comment 를 저장할 수 있다.")
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
        commentRepository.insert(comment);

        //when
        Comment findComment = commentRepository.findById(comment.getId()).orElse(null);

        //then
        assertCommentEquals(findComment, comment);

    }

    @DisplayName("comment 의 content 를 update 할 수 있다.")
    @Test
    void updateTitle() {
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
        commentRepository.update(comment.getId(), newContent);
        Comment findComment = commentRepository.findById(comment.getId()).orElse(null);

        //then
        assertThat(findComment).isNotNull();
        assertThat(findComment.getContent()).isEqualTo(newContent);
        assertThat(findComment.getUserId()).isEqualTo(user.getId());
        assertThat(findComment.getPostId()).isEqualTo(post.getId());
    }

    @DisplayName("post 와 관련된 모든 comment 를 조회할 수 있다.")
    @Test
    void updateContent() {
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


    @DisplayName("comment 를 삭제할 수 있다.")
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

}