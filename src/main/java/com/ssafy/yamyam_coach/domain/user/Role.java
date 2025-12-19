package com.ssafy.yamyam_coach.domain.user;

public enum Role {
    USER, ADMIN;
    
    // Spring Security는 권한 이름 앞에 "ROLE_"이 붙는 것을 좋아합니다.
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}