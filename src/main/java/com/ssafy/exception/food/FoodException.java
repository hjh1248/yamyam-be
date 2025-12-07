package com.ssafy.exception.food;

public class FoodException extends RuntimeException {
    public FoodException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
