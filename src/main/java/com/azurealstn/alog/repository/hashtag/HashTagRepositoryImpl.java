package com.azurealstn.alog.repository.hashtag;

import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.domain.hashtag.PostsHashTagMap;
import com.azurealstn.alog.domain.hashtag.QHashTag;
import com.azurealstn.alog.domain.hashtag.QPostsHashTagMap;
import com.azurealstn.alog.domain.posts.QPosts;
import com.azurealstn.alog.dto.hashtag.HashTagSearchDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.azurealstn.alog.domain.hashtag.QHashTag.*;
import static com.azurealstn.alog.domain.hashtag.QPostsHashTagMap.*;
import static com.azurealstn.alog.domain.posts.QPosts.*;

@RequiredArgsConstructor
public class HashTagRepositoryImpl implements HashTagRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<HashTag> findByTags(Long postsId) {
        return jpaQueryFactory
                .selectFrom(hashTag)
                .leftJoin(postsHashTagMap)
                .on(hashTag.id.eq(postsHashTagMap.hashTag.id))
                .leftJoin(posts)
                .on(postsHashTagMap.posts.id.eq(posts.id))
                .where(posts.id.eq(postsId))
                .fetch();
    }

    @Override
    public List<HashTag> findByTagsName(String name, HashTagSearchDto searchDto) {
        return jpaQueryFactory
                .selectFrom(hashTag)
                .leftJoin(postsHashTagMap)
                .on(hashTag.id.eq(postsHashTagMap.hashTag.id))
                .leftJoin(posts)
                .on(postsHashTagMap.posts.id.eq(posts.id))
                .where(hashTag.name.eq(name))
                .orderBy(posts.id.desc())
                .fetch();
    }

}
