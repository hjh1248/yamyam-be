package com.ssafy.yamyam_coach.repository.post;

import com.ssafy.yamyam_coach.IntegrationTestSupport;
import com.ssafy.yamyam_coach.domain.comment.Comment;
import com.ssafy.yamyam_coach.domain.dietplan.DietPlan;
import com.ssafy.yamyam_coach.domain.post.Post;
import com.ssafy.yamyam_coach.domain.user.User;
import com.ssafy.yamyam_coach.repository.comment.CommentRepository;
import com.ssafy.yamyam_coach.repository.diet_plan.DietPlanRepository;
import com.ssafy.yamyam_coach.repository.post.request.UpdatePostRepositoryRequest;
import com.ssafy.yamyam_coach.repository.post.response.CommentDetailResponse;
import com.ssafy.yamyam_coach.repository.post.response.DietPlanDetailResponse;
import com.ssafy.yamyam_coach.repository.post.response.PostDetailResponse;
import com.ssafy.yamyam_coach.repository.user.UserRepository;
import com.ssafy.yamyam_coach.util.DomainAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.ssafy.yamyam_coach.util.TestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class PostRepositoryTest extends IntegrationTestSupport {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DietPlanRepository dietPlanRepository;

    @Autowired
    CommentRepository commentRepository;

    @DisplayName("post 를 저장할 수 있다.")
    @Test
    void insert() {

        // given
        User user = createDummyUser();
        userRepository.save(user);

        DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan);

        Post post = createDummyPost(user.getId(), dietPlan.getId());
        postRepository.insert(post);

        //when
        Post findPost = postRepository.findById(post.getId()).orElse(null);

        //then
        DomainAssertions.assertPostEquals(findPost, post);

    }

    @DisplayName("post 의 제목만 update 할 수 있다.")
    @Test
    void updateTitle() {
        //given
        User user = createDummyUser();
        userRepository.save(user);

        DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan);

        Post post = createDummyPost(user.getId(), dietPlan.getId());
        postRepository.insert(post);

        String newTitle = "new title";

        UpdatePostRepositoryRequest updateRequest = UpdatePostRepositoryRequest.builder()
                .postId(post.getId())
                .title(newTitle)
                .build();

        //when
        postRepository.update(updateRequest);
        Post findPost = postRepository.findById(post.getId()).orElse(null);

        //then
        assertThat(findPost).isNotNull();
        assertThat(findPost.getTitle()).isEqualTo(newTitle);
        assertThat(findPost.getUserId()).isEqualTo(user.getId());
        assertThat(findPost.getDietPlanId()).isEqualTo(dietPlan.getId());
        assertThat(findPost.getContent()).isEqualTo(post.getContent());
    }

    @DisplayName("post 의 content만 update 할 수 있다.")
    @Test
    void updateContent() {
        //given
        User user = createDummyUser();
        userRepository.save(user);

        DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan);

        Post post = createDummyPost(user.getId(), dietPlan.getId());
        postRepository.insert(post);

        String newContent = "new content";

        UpdatePostRepositoryRequest updateRequest = UpdatePostRepositoryRequest.builder()
                .postId(post.getId())
                .content(newContent)
                .build();

        //when
        postRepository.update(updateRequest);
        Post findPost = postRepository.findById(post.getId()).orElse(null);

        //then
        assertThat(findPost).isNotNull();
        assertThat(findPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(findPost.getUserId()).isEqualTo(user.getId());
        assertThat(findPost.getDietPlanId()).isEqualTo(dietPlan.getId());
        assertThat(findPost.getContent()).isEqualTo(newContent);
    }

    @DisplayName("post 의 diet plan을 update 할 수 있다.")
    @Test
    void updateDietPlanToOtherPlan() {
        //given
        User user = createDummyUser();
        userRepository.save(user);

        DietPlan dietPlan1 = createDietPlan(user.getId(), "title1", "content1", false, true, LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan1);

        DietPlan dietPlan2 = createDietPlan(user.getId(), "title2", "content2", false, false, LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan2);


        Post post = createDummyPost(user.getId(), dietPlan1.getId());
        postRepository.insert(post);

        UpdatePostRepositoryRequest updateRequest = UpdatePostRepositoryRequest.builder()
                .postId(post.getId())
                .dietPlanId(dietPlan2.getId())
                .build();

        //when
        postRepository.update(updateRequest);
        Post findPost = postRepository.findById(post.getId()).orElse(null);

        //then
        assertThat(findPost).isNotNull();
        assertThat(findPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(findPost.getUserId()).isEqualTo(user.getId());
        assertThat(findPost.getDietPlanId()).isEqualTo(dietPlan2.getId());
        assertThat(findPost.getContent()).isEqualTo(post.getContent());
    }

    @DisplayName("post 의 diet plan을 연결 해제할 수 있다.")
    @Test
    void updateDietPlanToNull() {
        //given
        User user = createDummyUser();
        userRepository.save(user);

        DietPlan dietPlan1 = createDietPlan(user.getId(), "title1", "content1", false, true, LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan1);

        DietPlan dietPlan2 = createDietPlan(user.getId(), "title2", "content2", false, false, LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan2);


        Post post = createDummyPost(user.getId(), dietPlan1.getId());
        postRepository.insert(post);


        UpdatePostRepositoryRequest updateRequest = UpdatePostRepositoryRequest.builder()
                .postId(post.getId())
                .dietPlanId(-1L)
                .build();

        //when
        postRepository.update(updateRequest);
        Post findPost = postRepository.findById(post.getId()).orElse(null);

        //then
        assertThat(findPost).isNotNull();
        assertThat(findPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(findPost.getUserId()).isEqualTo(user.getId());
        assertThat(findPost.getDietPlanId()).isNull();
        assertThat(findPost.getContent()).isEqualTo(post.getContent());
    }

    @DisplayName("post 의 title, content, diet plan 을 모두 update 할 수 있다.")
    @Test
    void updateDietPlan() {
        //given
        User user = createDummyUser();
        userRepository.save(user);

        DietPlan dietPlan1 = createDietPlan(user.getId(), "title1", "content1", false, true, LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan1);

        DietPlan dietPlan2 = createDietPlan(user.getId(), "title2", "content2", false, false, LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan2);


        Post post = createDummyPost(user.getId(), dietPlan1.getId());
        postRepository.insert(post);

        String newTitle = "new title";
        String newContent = "new content";


        UpdatePostRepositoryRequest updateRequest = UpdatePostRepositoryRequest.builder()
                .postId(post.getId())
                .title(newTitle)
                .content(newContent)
                .dietPlanId(dietPlan2.getId())
                .build();

        //when
        postRepository.update(updateRequest);
        Post findPost = postRepository.findById(post.getId()).orElse(null);

        //then
        assertThat(findPost).isNotNull();
        assertThat(findPost.getTitle()).isEqualTo(newTitle);
        assertThat(findPost.getUserId()).isEqualTo(user.getId());
        assertThat(findPost.getDietPlanId()).isEqualTo(dietPlan2.getId());
        assertThat(findPost.getContent()).isEqualTo(newContent);
    }

    @DisplayName("post 를 삭제할 수 있다.")
    @Test
    void deleteById() {
        // given
        User user = createDummyUser();
        userRepository.save(user);

        DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan);

        Post post = createDummyPost(user.getId(), dietPlan.getId());
        postRepository.insert(post);


        // when
        int deleteCount = postRepository.deleteById(post.getId());
        Post findPost = postRepository.findById(post.getId()).orElse(null);

        //then
        assertThat(deleteCount).isEqualTo(1);
        assertThat(findPost).isNull();
    }

    @DisplayName("post 상세 조회시 작성자, 댓글, 연관 식단을 조회할 수 있다.")
    @Test
    void findPostDetail() {
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
        PostDetailResponse detail = postRepository.findPostDetail(post.getId()).orElse(null);

        //then
        assertThat(detail).isNotNull();
        assertThat(detail.getPostId()).isEqualTo(post.getId());
        assertThat(detail.getTitle()).isEqualTo(post.getTitle());
        assertThat(detail.getContent()).isEqualTo(post.getContent());
        assertThat(detail.getAuthor().getUserId()).isEqualTo(user.getId());
        assertThat(detail.getAuthor().getNickname()).isEqualTo(user.getNickname());

        DietPlanDetailResponse dietPlanDetail = detail.getDietPlan();
        assertThat(dietPlanDetail.getTitle()).isEqualTo(dietPlan.getTitle());
        assertThat(dietPlanDetail.getDietPlanId()).isEqualTo(post.getDietPlanId());
        assertThat(dietPlanDetail.getStartDate()).isEqualTo(dietPlan.getStartDate());
        assertThat(dietPlanDetail.getEndDate()).isEqualTo(dietPlan.getEndDate());

        List<CommentDetailResponse> comments = detail.getComments();
        assertThat(comments).hasSize(3)
                .extracting(CommentDetailResponse::getCommentId)
                .containsExactly(comment3.getId(), comment2.getId(), comment1.getId());
    }

}