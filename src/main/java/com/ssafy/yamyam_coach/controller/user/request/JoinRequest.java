package com.ssafy.yamyam_coach.controller.user.request;

import com.ssafy.yamyam_coach.service.user.request.UserCreateServiceRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinRequest {
    private String email;
    private String password;
    private String nickname;
    private String name;

    public UserCreateServiceRequest toServiceDto() {
        return UserCreateServiceRequest.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .name(this.name)
                .build();
    }
}