package com.ssafy.yamyam_coach.exception.comment;

import com.ssafy.yamyam_coach.exception.common.exception.CustomException;

public class CommentException extends CustomException {
    public CommentException(CommentErrorCode errorCode) {
        super(errorCode);
    }
}
