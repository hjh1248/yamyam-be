package com.ssafy.yamyam_coach.exception.meal;

import com.ssafy.yamyam_coach.exception.common.errorcode.CustomErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MealErrorCode implements CustomErrorCode {
    NOT_FOUND_MEAL("해당 식사를 조회할 수 없습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("식사를 생성할 권한이 없습니다.",  HttpStatus.FORBIDDEN),
    DUPLICATED_MEAL_TYPE("이미 해당 타입의 식사가 존재합니다.", HttpStatus.CONFLICT),
    ;

    private final String message;
    private final HttpStatus httpStatus;

    MealErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
