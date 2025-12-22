package com.ssafy.yamyam_coach.repository.challenge;

import com.ssafy.yamyam_coach.domain.challenge.Challenge;
import com.ssafy.yamyam_coach.domain.challenge_participation.ChallengeParticipation;
import com.ssafy.yamyam_coach.service.challenge.response.ChallengeResponse;

import java.util.List;

public interface ChallengeRepository {
    // 1. 챌린지 생성
    void saveChallenge(Challenge challenge);

    // 2. 조회 (내 챌린지 / 참여 가능 챌린지 / 단건 조회)
    List<ChallengeResponse> findMyChallenges(Long userId);
    List<ChallengeResponse> findAvailableChallenges(Long userId);

    // ★ 삭제 시 소유권(creatorId) 확인을 위해 필요함
    Challenge findById(Long id);

    // 3. 참여 및 포기 관리
    void saveParticipation(ChallengeParticipation participation);
    void updateParticipationStatus(Long userId, Long challengeId, String status);
    boolean existsParticipation(Long userId, Long challengeId);

    // 4. 챌린지 삭제 (소프트 삭제: status -> 'DELETED')
    void softDeleteChallenge(Long challengeId);

    List<Challenge> findAllById(List<Long> ids);
}