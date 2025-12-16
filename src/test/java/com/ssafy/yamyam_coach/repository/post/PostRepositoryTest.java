package com.ssafy.yamyam_coach.repository.post;

import com.ssafy.yamyam_coach.IntegrationTestSupport;
import com.ssafy.yamyam_coach.domain.dietplan.DietPlan;
import com.ssafy.yamyam_coach.domain.post.Post;
import com.ssafy.yamyam_coach.domain.user.User;
import com.ssafy.yamyam_coach.repository.diet_plan.DietPlanRepository;
import com.ssafy.yamyam_coach.repository.post.request.UpdatePostRepositoryRequest;
import com.ssafy.yamyam_coach.repository.user.UserRepository;
import com.ssafy.yamyam_coach.util.DomainAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static com.ssafy.yamyam_coach.util.TestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class PostRepositoryTest extends IntegrationTestSupport {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DietPlanRepository dietPlanRepository;

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

}