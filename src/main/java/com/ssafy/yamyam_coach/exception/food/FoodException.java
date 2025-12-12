package com.ssafy.yamyam_coach.exception.food;

import com.ssafy.yamyam_coach.exception.common.exception.CustomException;

public class FoodException extends CustomException {
    public FoodException(FoodErrorCode errorCode) {
        super(errorCode);
    }
}
