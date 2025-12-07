package com.ssafy.exception.daily_diet;

public class DailyDietException extends RuntimeException {
    public DailyDietException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
