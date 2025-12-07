package com.ssafy.exception.food;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_FOUND_FOOD("해당 음식을 조회할 수 없습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
