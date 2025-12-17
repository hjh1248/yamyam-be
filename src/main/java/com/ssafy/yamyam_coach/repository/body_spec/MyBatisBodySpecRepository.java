package com.ssafy.yamyam_coach.repository.body_spec;

import com.ssafy.yamyam_coach.domain.body_spec.BodySpec;
import com.ssafy.yamyam_coach.mapper.body_spec.BodySpecMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyBatisBodySpecRepository implements BodySpecRepository {

    private final BodySpecMapper bodySpecMapper;

    @Override
    public BodySpec save(BodySpec bodySpec) {
        bodySpecMapper.save(bodySpec);
        return bodySpec;
    }

    @Override
    public List<BodySpec> findAllByUserId(Long userId) {
        return bodySpecMapper.findAllByUserId(userId);
    }

    @Override
    public void delete(Long id) {
        bodySpecMapper.delete(id);
    }
}