package com.ssafy.yamyam_coach.exception.common.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonErrorCode implements CustomErrorCode{
    INVALID_INPUT_VALUE("입력이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus httpStatus;

    CommonErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
