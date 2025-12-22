package com.ssafy.yamyam_coach.security;

import jakarta.servlet.http.HttpServletRequest; // 스프링부트 3.x라면 jakarta, 2.x라면 javax
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, 
                         HttpServletResponse response, 
                         AuthenticationException authException) throws IOException {
        
        // 여기에 로그를 찍어보면 백엔드 콘솔에서 왜 에러가 났는지 알 수 있어
        System.out.println("❌ 인증 실패: " + authException.getMessage());

        // 핵심: 401 에러를 내려준다!
        // response.sendError(401, "Unauthorized"); 이렇게 숫자만 써도 되고,
        // 아래 상수를 쓰는 게 더 명확해 (SC_UNAUTHORIZED = 401)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증이 필요합니다.");
    }
}