package com.ssafy.yamyam_coach.service.user;

import com.ssafy.yamyam_coach.domain.user.User;
import com.ssafy.yamyam_coach.repository.user.UserRepository;
import com.ssafy.yamyam_coach.service.user.request.UserCreateServiceRequest;
import com.ssafy.yamyam_coach.service.user.response.UserLoginServiceResponse;
import com.ssafy.yamyam_coach.security.JwtTokenProvider;
import com.ssafy.yamyam_coach.service.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @Transactional
    public void signup(UserCreateServiceRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = request.toEntity(encodedPassword);
        
        userRepository.save(user);
    }

    // 로그인
    @Transactional(readOnly = true)
    public UserLoginServiceResponse login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getEmail());

        return UserLoginServiceResponse.builder()
                .accessToken(token)
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    // User 객체를 받아서 응답 DTO를 만들어주는 메서드
    @Transactional(readOnly = true)
    public UserResponse getUserProfile(User user) {
        // 나중에 여기에 팔로워 수 DB 조회 로직 추가 가능
        return new UserResponse(user);
    }
}