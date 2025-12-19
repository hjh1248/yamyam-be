package com.ssafy.yamyam_coach.mapper.user;

import com.ssafy.yamyam_coach.domain.user.User;
import com.ssafy.yamyam_coach.service.user.response.UserProfileResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    // 회원 저장 (void 대신 int를 쓰면 영향받은 행의 개수가 리턴돼서 성공 여부 체크 가능)
    int save(User user);

    // 이메일로 조회
    Optional<User> findByEmail(String email);

    // 닉네임 중복 체크
    boolean existsByNickname(String nickname);

    User findById(Long userId);

    List<User> searchByKeyword(@Param("keyword") String keyword);

    int deleteById(Long userId);

    // 타인 프로필 조회 (내 ID를 같이 넘겨서 팔로우 여부 확인)
    UserProfileResponse findUserProfile(@Param("targetId") Long targetId, @Param("myId") Long myId);

    // (참고) 유저 존재 확인용
    boolean existsById(Long id);
}
