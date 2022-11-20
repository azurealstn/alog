package com.azurealstn.alog.repository.like;

import com.azurealstn.alog.domain.hashtag.PostsHashTagMap;
import com.azurealstn.alog.domain.like.PostsLike;
import com.azurealstn.alog.repository.hashtag.PostsHashTagMapRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsLikeRepository extends JpaRepository<PostsLike, Long>, PostsLikeRepositoryCustom {
}
