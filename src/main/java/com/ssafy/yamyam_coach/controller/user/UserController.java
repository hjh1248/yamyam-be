package com.ssafy.yamyam_coach.controller.user;

import com.ssafy.yamyam_coach.controller.user.request.JoinRequest;
import com.ssafy.yamyam_coach.controller.user.request.LoginRequest;
import com.ssafy.yamyam_coach.service.user.UserService;
import com.ssafy.yamyam_coach.service.user.response.UserLoginServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody JoinRequest request) {
        userService.signup(request.toServiceDto());
        return ResponseEntity.ok("회원가입 성공!");
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginServiceResponse> login(@RequestBody LoginRequest request) {
        // 로그인은 입력값이 적어서 Service DTO 없이 바로 파라미터로 넘겨도 됨 (팀 스타일에 따라 유연하게)
        UserLoginServiceResponse response = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}