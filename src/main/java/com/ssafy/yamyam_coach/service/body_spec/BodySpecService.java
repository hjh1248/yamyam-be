package com.ssafy.yamyam_coach.service.body_spec;

import com.ssafy.yamyam_coach.domain.body_spec.BodySpec;
import com.ssafy.yamyam_coach.repository.body_spec.BodySpecRepository;
import com.ssafy.yamyam_coach.service.body_spec.request.BodySpecCreateServiceRequest;
import com.ssafy.yamyam_coach.service.body_spec.response.BodySpecServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BodySpecService {

    private final BodySpecRepository bodySpecRepository;

    // [수정] 이메일(String) 대신 ID(Long)를 받음
    @Transactional
    public void save(Long userId, BodySpecCreateServiceRequest request) {
        // 유저 조회 로직 삭제 (request.toEntity에 바로 userId 넣으면 됨)
        bodySpecRepository.save(request.toEntity(userId));
    }

    // [수정] 이메일(String) 대신 ID(Long)를 받음
    @Transactional(readOnly = true)
    public List<BodySpecServiceResponse> findAllByUserId(Long userId) {
        // 유저 조회 로직 삭제
        return bodySpecRepository.findAllByUserId(userId).stream()
                .map(BodySpecServiceResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        bodySpecRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<BodySpecServiceResponse> getBodySpecsByUserId(Long userId) {
        return bodySpecRepository.findAllByUserId(userId).stream()
                .map(BodySpecServiceResponse::of) // DTO 생성자 사용
                .collect(Collectors.toList());
    }
}