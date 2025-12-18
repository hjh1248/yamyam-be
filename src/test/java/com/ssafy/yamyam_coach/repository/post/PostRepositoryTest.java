package com.ssafy.yamyam_coach.repository.post;

import com.ssafy.yamyam_coach.IntegrationTestSupport;
import com.ssafy.yamyam_coach.domain.comment.Comment;
import com.ssafy.yamyam_coach.domain.dietplan.DietPlan;
import com.ssafy.yamyam_coach.domain.post.Post;
import com.ssafy.yamyam_coach.domain.postlike.PostLike;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Autowired
    com.ssafy.yamyam_coach.repository.postlike.PostLikeRepository postLikeRepository;

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
        PostDetailResponse detail = postRepository.findPostDetail(post.getId(), user.getId()).orElse(null);

        //then
        assertThat(detail).isNotNull();
        assertThat(detail.getPostId()).isEqualTo(post.getId());
        assertThat(detail.getTitle()).isEqualTo(post.getTitle());
        assertThat(detail.getContent()).isEqualTo(post.getContent());
        assertThat(detail.getLikeCount()).isEqualTo(0);
        assertThat(detail.getIsLiked()).isFalse();
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

    @DisplayName("post 상세 조회시 댓글이 없으면 빈 리스트가 반환된다.")
    @Test
    void findPostDetailWithEmptyComments() {
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

        //when
        PostDetailResponse detail = postRepository.findPostDetail(post.getId(), user.getId()).orElse(null);

        //then
        assertThat(detail).isNotNull();
        assertThat(detail.getPostId()).isEqualTo(post.getId());
        assertThat(detail.getTitle()).isEqualTo(post.getTitle());
        assertThat(detail.getContent()).isEqualTo(post.getContent());
        assertThat(detail.getLikeCount()).isEqualTo(0);
        assertThat(detail.getIsLiked()).isFalse();
        assertThat(detail.getAuthor().getUserId()).isEqualTo(user.getId());
        assertThat(detail.getAuthor().getNickname()).isEqualTo(user.getNickname());

        DietPlanDetailResponse dietPlanDetail = detail.getDietPlan();
        assertThat(dietPlanDetail.getTitle()).isEqualTo(dietPlan.getTitle());
        assertThat(dietPlanDetail.getDietPlanId()).isEqualTo(post.getDietPlanId());
        assertThat(dietPlanDetail.getStartDate()).isEqualTo(dietPlan.getStartDate());
        assertThat(dietPlanDetail.getEndDate()).isEqualTo(dietPlan.getEndDate());

        List<CommentDetailResponse> comments = detail.getComments();
        assertThat(comments).isEmpty();
    }

    @DisplayName("post 상세 조회시 연관 식단이 없을 경우 dietplan 은 null 이다.")
    @Test
    void findPostDetailWithEmptyDietPlan() {
        // given
        User user = createDummyUser();
        userRepository.save(user);

        Post post = createDummyPost(user.getId(), null);
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
        PostDetailResponse detail = postRepository.findPostDetail(post.getId(), user.getId()).orElse(null);

        //then
        assertThat(detail).isNotNull();
        assertThat(detail.getPostId()).isEqualTo(post.getId());
        assertThat(detail.getTitle()).isEqualTo(post.getTitle());
        assertThat(detail.getContent()).isEqualTo(post.getContent());
        assertThat(detail.getLikeCount()).isEqualTo(0);
        assertThat(detail.getIsLiked()).isFalse();
        assertThat(detail.getAuthor().getUserId()).isEqualTo(user.getId());
        assertThat(detail.getAuthor().getNickname()).isEqualTo(user.getNickname());

        DietPlanDetailResponse dietPlanDetail = detail.getDietPlan();
        assertThat(dietPlanDetail).isNull();

        List<CommentDetailResponse> comments = detail.getComments();
        assertThat(comments).hasSize(3)
                .extracting(CommentDetailResponse::getCommentId)
                .containsExactly(comment3.getId(), comment2.getId(), comment1.getId());
    }

    @Transactional(propagation =  Propagation.NOT_SUPPORTED)
    @Test
    @DisplayName("여러 사용자가 동시에 좋아요를 증가시킬 경우 정확한 개수가 반영된다.")
    void incrementLikeCount() throws Exception {

        // given
        User user = createDummyUser();
        userRepository.save(user);

        DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan);

        Post post = createDummyPost(user.getId(), dietPlan.getId());
        postRepository.insert(post);

        System.out.println("post id = " + post.getId());

        int threadCount = 100;
        ExecutorService es = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            es.execute(() -> {
                try {
                   postRepository.incrementLikeCount(post.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        es.shutdown();

        Post findPost = postRepository.findById(post.getId()).orElse(null);

        //then
        assertThat(findPost).isNotNull();
        assertThat(findPost.getLikeCount()).isEqualTo(threadCount);

        // cleansing
        postRepository.deleteById(post.getId());
        dietPlanRepository.deleteById(dietPlan.getId());
        userRepository.deleteById(user.getId());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("동시에 좋아요를 취소할 경우 누른 횟수만큼 좋아요가 감소해야 한다.")
    void unlikePostInMultiThread() throws Exception {

        //given
        User user = createDummyUser();
        userRepository.save(user);

        DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan);

        Post post = createDummyPost(user.getId(), dietPlan.getId());
        postRepository.insert(post);

        System.out.println("post id = " + post.getId());

        for (int i = 0; i < 100; i++) {
            postRepository.incrementLikeCount(post.getId());
        }

        int threadCount = 100;
        ExecutorService es = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            es.execute(() -> {
                try {
                     postRepository.decrementLikeCount(post.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        es.shutdown();

        // then
        Post findPost = postRepository.findById(post.getId()).orElseThrow();
        assertThat(findPost.getLikeCount()).isEqualTo(0);

        // cleansing
        postRepository.deleteById(post.getId());
        userRepository.deleteById(user.getId());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("좋아요 수는 음수가 될 수 없다.")
    void minimumIsZero() throws Exception {

        //given
        User user = createDummyUser();
        userRepository.save(user);

        DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan);

        Post post = createDummyPost(user.getId(), dietPlan.getId());
        postRepository.insert(post);

        System.out.println("post id = " + post.getId());

        for (int i = 0; i < 10; i++) {
            postRepository.incrementLikeCount(post.getId());
        }

        int threadCount = 100;
        ExecutorService es = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            es.execute(() -> {
                try {
                    postRepository.decrementLikeCount(post.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        es.shutdown();

        // then
        Post findPost = postRepository.findById(post.getId()).orElseThrow();
        assertThat(findPost.getLikeCount()).isEqualTo(0);

        // cleansing
        postRepository.deleteById(post.getId());
        userRepository.deleteById(user.getId());
    }

    @DisplayName("사용자가 좋아요를 누른 게시글을 조회하면 isLiked가 true이다.")
    @Test
    void findPostDetailWithLike() {
        // given
        User user = createDummyUser();
        userRepository.save(user);

        DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan);

        Post post = createDummyPost(user.getId(), dietPlan.getId());
        postRepository.insert(post);

        // 좋아요 추가
        PostLike postLike = PostLike.builder()
                .postId(post.getId())
                .userId(user.getId())
                .createdAt(java.time.LocalDateTime.now())
                .build();
        postLikeRepository.insert(postLike);

        // when
        PostDetailResponse detail = postRepository.findPostDetail(post.getId(), user.getId()).orElse(null);

        // then
        assertThat(detail).isNotNull();
        assertThat(detail.getIsLiked()).isTrue();
        assertThat(detail.getLikeCount()).isEqualTo(0);
    }

    @DisplayName("사용자가 좋아요를 누르지 않은 게시글을 조회하면 isLiked가 false이다.")
    @Test
    void findPostDetailWithoutLike() {
        // given
        User user = createDummyUser();
        userRepository.save(user);

        DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan);

        Post post = createDummyPost(user.getId(), dietPlan.getId());
        postRepository.insert(post);

        // when
        PostDetailResponse detail = postRepository.findPostDetail(post.getId(), user.getId()).orElse(null);

        // then
        assertThat(detail).isNotNull();
        assertThat(detail.getIsLiked()).isFalse();
        assertThat(detail.getLikeCount()).isEqualTo(0);
    }

    @DisplayName("로그인하지 않은 사용자가 게시글을 조회하면 isLiked가 false이다.")
    @Test
    void findPostDetailWithNullUser() {
        // given
        User user = createDummyUser();
        userRepository.save(user);

        DietPlan dietPlan = createDummyDietPlan(user.getId(), LocalDate.now(), LocalDate.now().plusDays(1));
        dietPlanRepository.insert(dietPlan);

        Post post = createDummyPost(user.getId(), dietPlan.getId());
        postRepository.insert(post);

        // 좋아요 추가
        PostLike postLike = com.ssafy.yamyam_coach.domain.postlike.PostLike.builder()
                .postId(post.getId())
                .userId(user.getId())
                .createdAt(java.time.LocalDateTime.now())
                .build();
        postLikeRepository.insert(postLike);

        // when
        PostDetailResponse detail = postRepository.findPostDetail(post.getId(), null).orElse(null);

        // then
        assertThat(detail).isNotNull();
        assertThat(detail.getIsLiked()).isFalse();
        assertThat(detail.getLikeCount()).isEqualTo(0);
    }

}