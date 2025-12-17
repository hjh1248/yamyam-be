package com.ssafy.yamyam_coach.mapper.post;

import com.ssafy.yamyam_coach.domain.post.Post;
import com.ssafy.yamyam_coach.repository.post.request.UpdatePostRepositoryRequest;
import com.ssafy.yamyam_coach.repository.post.response.PostDetailResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {

    int insert(Post post);

    Post findById(Long postId);

    int update(UpdatePostRepositoryRequest request);

    int deleteById(Long postId);

    PostDetailResponse findPostDetail(Long postId);

    int incrementLikeCount(Long postId);
}
