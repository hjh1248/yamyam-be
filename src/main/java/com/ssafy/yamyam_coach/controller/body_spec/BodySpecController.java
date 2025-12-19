package com.ssafy.yamyam_coach.controller.body_spec;

import com.ssafy.yamyam_coach.domain.user.User; // User 엔티티 import
import com.ssafy.yamyam_coach.controller.body_spec.request.BodySpecCreateRequest;
import com.ssafy.yamyam_coach.global.annotation.LoginUser;
import com.ssafy.yamyam_coach.service.body_spec.BodySpecService;
import com.ssafy.yamyam_coach.service.body_spec.response.BodySpecServiceResponse;
// import com.ssafy.yamyam_coach.global.annotation.LoginUser; // 네가 만든 어노테이션 위치에 맞게 import
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/body-specs")
@RequiredArgsConstructor
public class BodySpecController {

    private final BodySpecService bodySpecService;

    // 1. 목록 조회
    @GetMapping
    public ResponseEntity<List<BodySpecServiceResponse>> getMyBodySpecs(@LoginUser User user) {
        if (user == null) {
            // 혹은 Global Exception Handler가 있다면 throw new UnAuthorizedException() 등으로 처리
            return ResponseEntity.status(401).build();
        }
        // 서비스에 이메일 대신 'userId'를 바로 넘김! (효율 Up 🚀)
        return ResponseEntity.ok(bodySpecService.findAllByUserId(user.getId()));
    }

    // 2. 추가
    @PostMapping
    public ResponseEntity<String> addBodySpec(
            @LoginUser User user,
            @RequestBody BodySpecCreateRequest request) {

        if (user == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        // 서비스에 'userId' 바로 넘김
        bodySpecService.save(user.getId(), request.toServiceDto());
        return ResponseEntity.ok("추가되었습니다.");
    }

    // 3. 삭제 (이건 로그인 유저 체크 로직 추가하면 좋음)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBodySpec(@LoginUser User user, @PathVariable Long id) {
        if (user == null) return ResponseEntity.status(401).build();

        // (심화) 여기서 user.getId()랑 삭제하려는 데이터의 주인이 같은지 확인하는 로직 넣으면 더 좋음
        bodySpecService.delete(id);
        return ResponseEntity.ok("삭제되었습니다.");
    }

    // 타인의 신체 정보 목록 조회 (수정/삭제 기능은 없음)
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<BodySpecServiceResponse>> getUserBodySpecs(@PathVariable Long userId) {
        return ResponseEntity.ok(bodySpecService.getBodySpecsByUserId(userId));
    }
}