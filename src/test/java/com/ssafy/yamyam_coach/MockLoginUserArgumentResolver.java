package com.ssafy.yamyam_coach;

import com.ssafy.yamyam_coach.domain.user.User;
import com.ssafy.yamyam_coach.global.annotation.LoginUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MockLoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final User mockUser;

    public MockLoginUserArgumentResolver(User mockUser) {
        this.mockUser = mockUser;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class) &&
                parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return mockUser;
    }
}
