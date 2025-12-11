package com.ssafy.yamyam_coach.exception.daily_diet;

import com.ssafy.yamyam_coach.exception.common.exception.CustomException;

public class DailyDietException extends CustomException {

    public DailyDietException(ErrorCode errorCode) {
        super(errorCode);
    }
}
