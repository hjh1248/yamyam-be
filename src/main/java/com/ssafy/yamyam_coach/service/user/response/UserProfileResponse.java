package com.ssafy.yamyam_coach.service.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder            // ★ [필수] 이게 있어야 builder()를 쓸 수 있습니다!
@NoArgsConstructor  // ★ [필수] 기본 생성자 (JSON 변환 등에 필요)
@AllArgsConstructor // ★ [필수] 모든 인자 생성자 (@Builder가 이걸 내부적으로 사용함)
public class UserProfileResponse {
    private Long id;
    private String email;
    private String nickname;
    private String name;
    private int followers;
    private int following;

    @JsonProperty("isFollowing")
    private boolean isFollowing;
}