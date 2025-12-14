package com.ssafy.yamyam_coach;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssafy.yamyam_coach.domain.user.User;
import com.ssafy.yamyam_coach.security.CustomUserDetailsService;
import com.ssafy.yamyam_coach.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

public abstract class RestControllerTestSupport {

    protected final User mockUser = User.createMockUser();

    protected MockMvc mockMvc;

    @MockitoBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    protected CustomUserDetailsService customUserDetailsService;

    @Autowired
    protected ObjectMapper objectMapper;
}
