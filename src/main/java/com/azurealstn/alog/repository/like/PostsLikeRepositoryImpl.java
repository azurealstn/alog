package com.azurealstn.alog.repository.like;

import com.azurealstn.alog.domain.like.PostsLike;
import com.azurealstn.alog.domain.like.QPostsLike;
import com.azurealstn.alog.repository.hashtag.PostsHashTagMapRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.azurealstn.alog.domain.hashtag.QPostsHashTagMap.postsHashTagMap;
import static com.azurealstn.alog.domain.like.QPostsLike.*;

@RequiredArgsConstructor
public class PostsLikeRepositoryImpl implements PostsLikeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<PostsLike> exist(Long memberId, Long postsId) {
        PostsLike postsLike = jpaQueryFactory
                .selectFrom(QPostsLike.postsLike)
                .where(QPostsLike.postsLike.member.id.eq(memberId),
                        QPostsLike.postsLike.posts.id.eq(postsId))
                .fetchFirst();

        return Optional.ofNullable(postsLike);
    }

    @Override
    public int findPostsLikeCount(Long postsId) {
        return jpaQueryFactory
                .selectFrom(postsLike)
                .where(postsLike.posts.id.eq(postsId))
                .fetch().size();
    }
}
