package com.ssafy.yamyam_coach.exception.meal;

import com.ssafy.yamyam_coach.exception.common.exception.CustomException;

public class MealException extends CustomException {
    public MealException(MealErrorCode errorCode) {
        super(errorCode);
    }
}
