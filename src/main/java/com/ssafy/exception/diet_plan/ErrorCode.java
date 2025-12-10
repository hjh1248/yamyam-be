package com.ssafy.exception.diet_plan;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_FOUND_DIET_PLAN("해당 식단 계획을 조회할 수 없습니다."),
    NOT_FOUND_PRIMARY_DIET_PLAN("사용자의 대표 식단을 찾을 수 없습니다"),
    UNAUTHORIZED("식단 계획 삭제 권한이 없습니다."),
    CANNOT_SET_AS_PRIMARY("해당 식단 계획을 대표 식단 계획으로 설정할 수 없습니다.")
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
