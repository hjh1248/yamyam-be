package com.ssafy.yamyam_coach.exception.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.yamyam_coach.exception.common.errorcode.CustomErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null 인 필드는 json 에서 제외
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String reasonPhrase;
    private final String message;
    private final List<MyFieldError> fieldErrors;

    public static ErrorResponse of(CustomErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .reasonPhrase(errorCode.getHttpStatus().getReasonPhrase())
                .message(message)
                .build();
    }

    public static ErrorResponse of(CustomErrorCode errorCode, BindingResult bindingResult) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .reasonPhrase(errorCode.getHttpStatus().getReasonPhrase())
                .message("입력 값 유효성 검사에 실패했습니다.") // 공통 메시지
                .fieldErrors(MyFieldError.of(bindingResult)) // 필드별 상세 오류 목록
                .build();
    }

    @Getter
    @Builder
    static class MyFieldError {
        private final String field;
        private final String value;
        private final String reason;

        public static List<MyFieldError> of(BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                    .map(e -> MyFieldError.builder()
                            .field(e.getField())
                            .value(e.getDefaultMessage())
                            .reason(e.getDefaultMessage())
                            .build())
                    .toList();
        }
    }
}
