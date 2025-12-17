package com.ssafy.yamyam_coach.mapper.body_spec;

import com.ssafy.yamyam_coach.domain.body_spec.BodySpec; // 네가 만든 파일 import
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BodySpecMapper {
    void save(BodySpec bodySpec);
    List<BodySpec> findAllByUserId(Long userId);
    void delete(Long id);
}