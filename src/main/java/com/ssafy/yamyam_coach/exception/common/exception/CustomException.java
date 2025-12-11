package com.ssafy.yamyam_coach.exception.common.exception;


import com.ssafy.yamyam_coach.exception.common.errorcode.CustomErrorCode;
import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    private final CustomErrorCode errorCode;

    public CustomException(CustomErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
