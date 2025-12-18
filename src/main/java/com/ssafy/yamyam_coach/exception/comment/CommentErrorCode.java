package com.ssafy.yamyam_coach.exception.comment;

import com.ssafy.yamyam_coach.exception.common.errorcode.CustomErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommentErrorCode implements CustomErrorCode {
    NOT_FOUND_COMMENT("해당 댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND_IN_POST("해당 게시글에 해당 댓글을 조회할 수 없습니다.", HttpStatus.NOT_FOUND),
    ACCESS_DENIED_COMMENT("해당 댓글에 대한 작업 권한이 없습니다.", HttpStatus.FORBIDDEN)
    ;

    private final String message;
    private final HttpStatus httpStatus;

    CommentErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
