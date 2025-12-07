package com.ssafy.exception.diet_plan;

public class DietPlanException extends RuntimeException {
    public DietPlanException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
