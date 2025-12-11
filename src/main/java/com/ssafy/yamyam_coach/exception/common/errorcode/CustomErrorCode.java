package com.ssafy.yamyam_coach.exception.common.errorcode;

import org.springframework.http.HttpStatus;

public interface CustomErrorCode {
    String getMessage();
    HttpStatus getHttpStatus();
}
