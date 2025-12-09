package com.ssafy.yamyam_coach.service.user.request;

import com.ssafy.yamyam_coach.domain.user.User;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserCreateServiceRequest {

    private String email;
    private String password;
    private String nickname;
    private String name;

    @Builder
    private UserCreateServiceRequest(String email, String password, String nickname, String name) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
    }

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .nickname(this.nickname)
                .name(this.name)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}