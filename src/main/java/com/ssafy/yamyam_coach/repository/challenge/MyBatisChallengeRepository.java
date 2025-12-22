package com.ssafy.yamyam_coach.repository.challenge;

import com.ssafy.yamyam_coach.domain.challenge.Challenge;
import com.ssafy.yamyam_coach.domain.challenge_participation.ChallengeParticipation;
import com.ssafy.yamyam_coach.mapper.challenge.ChallengeMapper;
import com.ssafy.yamyam_coach.service.challenge.response.ChallengeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyBatisChallengeRepository implements ChallengeRepository {

    private final ChallengeMapper challengeMapper;

    @Override
    public void saveChallenge(Challenge challenge) {
        challengeMapper.saveChallenge(challenge);
    }

    @Override
    public List<ChallengeResponse> findMyChallenges(Long userId) {
        return challengeMapper.findMyChallenges(userId);
    }

    @Override
    public List<ChallengeResponse> findAvailableChallenges(Long userId) {
        return challengeMapper.findAvailableChallenges(userId);
    }

    @Override
    public Challenge findById(Long id) {
        return challengeMapper.findById(id);
    }

    @Override
    public void saveParticipation(ChallengeParticipation participation) {
        challengeMapper.saveParticipation(participation);
    }

    @Override
    public void updateParticipationStatus(Long userId, Long challengeId, String status) {
        challengeMapper.updateParticipationStatus(userId, challengeId, status);
    }

    @Override
    public boolean existsParticipation(Long userId, Long challengeId) {
        return challengeMapper.existsParticipation(userId, challengeId);
    }

    @Override
    public void softDeleteChallenge(Long challengeId) {
        challengeMapper.softDeleteChallenge(challengeId);
    }

    @Override
    public List<Challenge> findAllById(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return challengeMapper.findAllById(ids);
    }
}