package com.ssafy.yamyam_coach.security;

import com.ssafy.yamyam_coach.domain.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // ★ 필수 임포트
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // 필수 오버라이드 메서드들
    @Override
    public String getUsername() { return user.getEmail(); }

    @Override
    public String getPassword() { return user.getPassword(); }

    /**
     * ★ 핵심 수정 부분 ★
     * DB에 있는 Role 정보를 Spring Security가 이해하는 '권한(Authority)'으로 변환해서 줍니다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 목록을 담을 리스트 생성
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // 1. User 객체에 role이 있는지 확인 (null 방지)
        if (user.getRole() != null) {
            // 2. DB의 Role -> SimpleGrantedAuthority 변환
            // Role Enum에 만들어둔 getAuthority() 메서드("ROLE_ADMIN" 등 반환)를 사용
            // 만약 Enum에 메서드가 없다면: new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
            authorities.add(new SimpleGrantedAuthority(user.getRole().getAuthority()));
        } else {
            // (선택 사항) Role이 null이면 기본 'ROLE_USER'라도 쥐어줍니다.
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return authorities;
    }

    // 계정 상태 관리
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}