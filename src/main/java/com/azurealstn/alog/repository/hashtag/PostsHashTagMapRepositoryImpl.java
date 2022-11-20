package com.azurealstn.alog.repository.hashtag;

import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.domain.hashtag.QPostsHashTagMap;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.azurealstn.alog.domain.hashtag.QHashTag.hashTag;
import static com.azurealstn.alog.domain.hashtag.QPostsHashTagMap.postsHashTagMap;
import static com.azurealstn.alog.domain.posts.QPosts.posts;

@RequiredArgsConstructor
public class PostsHashTagMapRepositoryImpl implements PostsHashTagMapRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public void deleteByPostsId(Long postsId) {
        jpaQueryFactory.delete(postsHashTagMap)
                .where(postsHashTagMap.posts.id.eq(postsId))
                .execute();
    }
}
