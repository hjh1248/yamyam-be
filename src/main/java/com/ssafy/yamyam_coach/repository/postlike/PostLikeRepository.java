package com.ssafy.yamyam_coach.repository.postlike;

import com.ssafy.yamyam_coach.domain.postlike.PostLike;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository {

    int insert(PostLike postLike);

    Optional<PostLike> findById(Long postLikeId);

    Optional<PostLike> findByPostAndUser(Long postId, Long userId);

    List<PostLike> findByPost(Long postId);

    int deleteByPostAndUser(Long postId, Long userId);
}
