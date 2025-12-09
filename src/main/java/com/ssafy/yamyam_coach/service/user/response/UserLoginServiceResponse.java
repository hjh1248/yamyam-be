package com.ssafy.yamyam_coach.service.user.response;

import lombok.Builder;
import lombok.Data;

@Data
public class UserLoginServiceResponse {

    private String accessToken;
    private String email;
    private String nickname;

    @Builder
    private UserLoginServiceResponse(String accessToken, String email, String nickname) {
        this.accessToken = accessToken;
        this.email = email;
        this.nickname = nickname;
    }
}