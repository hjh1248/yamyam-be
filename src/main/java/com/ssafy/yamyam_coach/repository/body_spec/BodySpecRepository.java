package com.ssafy.yamyam_coach.repository.body_spec;

import com.ssafy.yamyam_coach.domain.body_spec.BodySpec;
import java.util.List;

public interface BodySpecRepository {
    BodySpec save(BodySpec bodySpec);
    List<BodySpec> findAllByUserId(Long userId);
    void delete(Long id);
    List<BodySpec> findAllById(List<Long> ids);
}