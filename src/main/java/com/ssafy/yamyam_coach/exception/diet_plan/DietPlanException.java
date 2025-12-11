package com.ssafy.yamyam_coach.exception.diet_plan;

import com.ssafy.yamyam_coach.exception.common.exception.CustomException;

public class DietPlanException extends CustomException {
    public DietPlanException(ErrorCode errorCode) {
        super(errorCode);
    }
}
