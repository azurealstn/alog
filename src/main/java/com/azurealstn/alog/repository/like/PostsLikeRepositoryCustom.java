package com.azurealstn.alog.repository.like;

import com.azurealstn.alog.domain.like.PostsLike;

import java.util.Optional;

public interface PostsLikeRepositoryCustom {

    /**
     * 좋아요를 눌렀는지 체크
     */
    Optional<PostsLike> exist(Long memberId, Long postsId);

    /**
     * 좋아요 총 개수
     */
    int findPostsLikeCount(Long postsId);

}
