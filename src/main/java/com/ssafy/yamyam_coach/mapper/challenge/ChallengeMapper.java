package com.ssafy.yamyam_coach.mapper.challenge;

import com.ssafy.yamyam_coach.domain.challenge.Challenge;
import com.ssafy.yamyam_coach.domain.challenge_participation.ChallengeParticipation;
import com.ssafy.yamyam_coach.service.challenge.response.ChallengeResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ChallengeMapper {
    // 생성
    void saveChallenge(Challenge challenge);

    // 조회
    List<ChallengeResponse> findMyChallenges(Long userId);
    List<ChallengeResponse> findAvailableChallenges(Long userId);
    Challenge findById(Long id); // 소유권 확인용

    // 참여/포기
    void saveParticipation(ChallengeParticipation participation);
    void updateParticipationStatus(@Param("userId") Long userId, @Param("challengeId") Long challengeId, @Param("status") String status);
    boolean existsParticipation(@Param("userId") Long userId, @Param("challengeId") Long challengeId);

    // 삭제 (소프트)
    void softDeleteChallenge(Long challengeId);
}