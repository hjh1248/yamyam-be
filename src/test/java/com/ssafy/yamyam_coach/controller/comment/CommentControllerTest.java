package com.ssafy.yamyam_coach.controller.comment;

import com.ssafy.yamyam_coach.MockLoginUserArgumentResolver;
import com.ssafy.yamyam_coach.RestControllerTestSupport;
import com.ssafy.yamyam_coach.controller.comment.request.CreateCommentRequest;
import com.ssafy.yamyam_coach.controller.comment.request.UpdateCommentRequest;
import com.ssafy.yamyam_coach.exception.comment.CommentException;
import com.ssafy.yamyam_coach.exception.common.advice.GlobalRestExceptionHandler;
import com.ssafy.yamyam_coach.exception.post.PostException;
import com.ssafy.yamyam_coach.service.comment.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.ssafy.yamyam_coach.exception.comment.CommentErrorCode.*;
import static com.ssafy.yamyam_coach.exception.post.PostErrorCode.NOT_FOUND_POST;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest extends RestControllerTestSupport {

    @MockitoBean
    CommentService commentService;

    @BeforeEach
    void setUp() {
        MockLoginUserArgumentResolver mockLoginUserArgumentResolver = new MockLoginUserArgumentResolver(mockUser);

        mockMvc = MockMvcBuilders.standaloneSetup(new CommentController(commentService))
                .setCustomArgumentResolvers(mockLoginUserArgumentResolver)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("registerComment")
    class RegisterComment {

        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase {

            @DisplayName("유효한 요청으로 댓글 등록 시 200 OK를 반환한다")
            @Test
            void registerComment() throws Exception {
                // given
                Long postId = 1L;

                CreateCommentRequest request = new CreateCommentRequest();
                request.setContent("댓글 내용");

                // stubbing
                given(commentService.registerComment(anyLong(), anyLong(), anyString()))
                        .willReturn(1L);

                // when then
                mockMvc.perform(
                                post("/api/posts/{postId}/comments", postId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request))
                        )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").doesNotExist());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailureCase {

            @DisplayName("content가 빈 문자열일 경우 400 응답이 반환된다")
            @Test
            void contentIsBlank() throws Exception {
                // given
                Long postId = 1L;

                CreateCommentRequest request = new CreateCommentRequest();
                request.setContent("");

                // when then
                mockMvc.perform(
                                post("/api/posts/{postId}/comments", postId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request))
                        )
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath("$.fieldErrors").isArray())
                        .andExpect(jsonPath("$.timestamp").isNotEmpty());
            }

            @DisplayName("content가 null일 경우 400 응답이 반환된다")
            @Test
            void contentIsNull() throws Exception {
                // given
                Long postId = 1L;

                CreateCommentRequest request = new CreateCommentRequest();
                request.setContent(null);

                // when then
                mockMvc.perform(
                                post("/api/posts/{postId}/comments", postId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request))
                        )
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath("$.fieldErrors").isArray())
                        .andExpect(jsonPath("$.timestamp").isNotEmpty());
            }

            @DisplayName("존재하지 않는 게시글에 댓글 등록 시 404 응답이 반환된다")
            @Test
            void registerCommentToNotExistingPost() throws Exception {
                // given
                Long postId = 999L;

                CreateCommentRequest request = new CreateCommentRequest();
                request.setContent("댓글 내용");

                // stubbing
                given(commentService.registerComment(anyLong(), anyLong(), anyString()))
                        .willThrow(new PostException(NOT_FOUND_POST));

                // when then
                mockMvc.perform(
                                post("/api/posts/{postId}/comments", postId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request))
                        )
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("해당 게시글을 찾을 수 없습니다."))
                        .andExpect(jsonPath("$.timestamp").isNotEmpty());
            }
        }
    }

    @Nested
    @DisplayName("updateComment")
    class UpdateComment {

        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase {

            @DisplayName("유효한 요청으로 댓글 수정 시 200 OK를 반환한다")
            @Test
            void updateComment() throws Exception {
                // given
                Long postId = 1L;
                Long commentId = 1L;

                UpdateCommentRequest request = new UpdateCommentRequest();
                request.setNewContent("수정된 댓글 내용");

                // stubbing
                doNothing().when(commentService).updateComment(anyLong(), anyLong(), anyLong(), anyString());

                // when then
                mockMvc.perform(
                                patch("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request))
                        )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").doesNotExist());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailureCase {

            @DisplayName("newContent가 빈 문자열일 경우 400 응답이 반환된다")
            @Test
            void newContentIsBlank() throws Exception {
                // given
                Long postId = 1L;
                Long commentId = 1L;

                UpdateCommentRequest request = new UpdateCommentRequest();
                request.setNewContent("");

                // when then
                mockMvc.perform(
                                patch("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request))
                        )
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath("$.fieldErrors").isArray())
                        .andExpect(jsonPath("$.timestamp").isNotEmpty());
            }

            @DisplayName("newContent가 null일 경우 400 응답이 반환된다")
            @Test
            void newContentIsNull() throws Exception {
                // given
                Long postId = 1L;
                Long commentId = 1L;

                UpdateCommentRequest request = new UpdateCommentRequest();
                request.setNewContent(null);

                // when then
                mockMvc.perform(
                                patch("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request))
                        )
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath("$.fieldErrors").isArray())
                        .andExpect(jsonPath("$.timestamp").isNotEmpty());
            }

            @DisplayName("존재하지 않는 게시글의 댓글 수정 시 404 응답이 반환된다")
            @Test
            void updateCommentOfNotExistingPost() throws Exception {
                // given
                Long postId = 999L;
                Long commentId = 1L;

                UpdateCommentRequest request = new UpdateCommentRequest();
                request.setNewContent("수정된 댓글 내용");

                // stubbing
                doThrow(new PostException(NOT_FOUND_POST))
                        .when(commentService).updateComment(anyLong(), anyLong(), anyLong(), anyString());

                // when then
                mockMvc.perform(
                                patch("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request))
                        )
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("해당 게시글을 찾을 수 없습니다."))
                        .andExpect(jsonPath("$.timestamp").isNotEmpty());
            }

            @DisplayName("존재하지 않는 댓글 수정 시 404 응답이 반환된다")
            @Test
            void updateNotExistingComment() throws Exception {
                // given
                Long postId = 1L;
                Long commentId = 999L;

                UpdateCommentRequest request = new UpdateCommentRequest();
                request.setNewContent("수정된 댓글 내용");

                // stubbing
                doThrow(new CommentException(NOT_FOUND_COMMENT))
                        .when(commentService).updateComment(anyLong(), anyLong(), anyLong(), anyString());

                // when then
                mockMvc.perform(
                                patch("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request))
                        )
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("해당 댓글을 찾을 수 없습니다."))
                        .andExpect(jsonPath("$.timestamp").isNotEmpty());
            }

            @DisplayName("다른 게시글의 댓글 수정 시 404 응답이 반환된다")
            @Test
            void updateCommentOfDifferentPost() throws Exception {
                // given
                Long postId = 2L;
                Long commentId = 1L;

                UpdateCommentRequest request = new UpdateCommentRequest();
                request.setNewContent("수정된 댓글 내용");

                // stubbing
                doThrow(new CommentException(COMMENT_NOT_FOUND_IN_POST))
                        .when(commentService).updateComment(anyLong(), anyLong(), anyLong(), anyString());

                // when then
                mockMvc.perform(
                                patch("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request))
                        )
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("해당 게시글에 해당 댓글을 조회할 수 없습니다."))
                        .andExpect(jsonPath("$.timestamp").isNotEmpty());
            }

            @DisplayName("다른 사용자의 댓글 수정 시 403 응답이 반환된다")
            @Test
            void updateOtherUsersComment() throws Exception {
                // given
                Long postId = 1L;
                Long commentId = 1L;

                UpdateCommentRequest request = new UpdateCommentRequest();
                request.setNewContent("수정된 댓글 내용");

                // stubbing
                doThrow(new CommentException(ACCESS_DENIED_COMMENT))
                        .when(commentService).updateComment(anyLong(), anyLong(), anyLong(), anyString());

                // when then
                mockMvc.perform(
                                patch("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request))
                        )
                        .andDo(print())
                        .andExpect(status().isForbidden())
                        .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                        .andExpect(jsonPath("$.message").value("해당 댓글에 대한 작업 권한이 없습니다."))
                        .andExpect(jsonPath("$.timestamp").isNotEmpty());
            }
        }
    }

    @Nested
    @DisplayName("deleteComment")
    class DeleteComment {

        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase {

            @DisplayName("유효한 요청으로 댓글 삭제 시 200 OK를 반환한다")
            @Test
            void deleteComment() throws Exception {
                // given
                Long postId = 1L;
                Long commentId = 1L;

                // stubbing
                doNothing().when(commentService).deleteComment(anyLong(), anyLong(), anyLong());

                // when then
                mockMvc.perform(
                                delete("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                        )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").doesNotExist());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailureCase {

            @DisplayName("존재하지 않는 게시글의 댓글 삭제 시 404 응답이 반환된다")
            @Test
            void deleteCommentOfNotExistingPost() throws Exception {
                // given
                Long postId = 999L;
                Long commentId = 1L;

                // stubbing
                doThrow(new PostException(NOT_FOUND_POST))
                        .when(commentService).deleteComment(anyLong(), anyLong(), anyLong());

                // when then
                mockMvc.perform(
                                delete("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                        )
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("해당 게시글을 찾을 수 없습니다."))
                        .andExpect(jsonPath("$.timestamp").isNotEmpty());
            }

            @DisplayName("존재하지 않는 댓글 삭제 시 404 응답이 반환된다")
            @Test
            void deleteNotExistingComment() throws Exception {
                // given
                Long postId = 1L;
                Long commentId = 999L;

                // stubbing
                doThrow(new CommentException(NOT_FOUND_COMMENT))
                        .when(commentService).deleteComment(anyLong(), anyLong(), anyLong());

                // when then
                mockMvc.perform(
                                delete("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                        )
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("해당 댓글을 찾을 수 없습니다."))
                        .andExpect(jsonPath("$.timestamp").isNotEmpty());
            }

            @DisplayName("다른 게시글의 댓글 삭제 시 404 응답이 반환된다")
            @Test
            void deleteCommentOfDifferentPost() throws Exception {
                // given
                Long postId = 2L;
                Long commentId = 1L;

                // stubbing
                doThrow(new CommentException(COMMENT_NOT_FOUND_IN_POST))
                        .when(commentService).deleteComment(anyLong(), anyLong(), anyLong());

                // when then
                mockMvc.perform(
                                delete("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                        )
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                        .andExpect(jsonPath("$.message").value("해당 게시글에 해당 댓글을 조회할 수 없습니다."))
                        .andExpect(jsonPath("$.timestamp").isNotEmpty());
            }

            @DisplayName("다른 사용자의 댓글 삭제 시 403 응답이 반환된다")
            @Test
            void deleteOtherUsersComment() throws Exception {
                // given
                Long postId = 1L;
                Long commentId = 1L;

                // stubbing
                doThrow(new CommentException(ACCESS_DENIED_COMMENT))
                        .when(commentService).deleteComment(anyLong(), anyLong(), anyLong());

                // when then
                mockMvc.perform(
                                delete("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                        )
                        .andDo(print())
                        .andExpect(status().isForbidden())
                        .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                        .andExpect(jsonPath("$.message").value("해당 댓글에 대한 작업 권한이 없습니다."))
                        .andExpect(jsonPath("$.timestamp").isNotEmpty());
            }
        }
    }
}
