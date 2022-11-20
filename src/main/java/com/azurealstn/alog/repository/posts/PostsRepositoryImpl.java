package com.azurealstn.alog.repository.posts;

import com.azurealstn.alog.domain.like.QPostsLike;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.domain.posts.QPosts;
import com.azurealstn.alog.dto.hashtag.HashTagSearchDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.azurealstn.alog.domain.hashtag.QHashTag.hashTag;
import static com.azurealstn.alog.domain.hashtag.QPostsHashTagMap.postsHashTagMap;
import static com.azurealstn.alog.domain.like.QPostsLike.*;
import static com.azurealstn.alog.domain.posts.QPosts.*;

@RequiredArgsConstructor
public class PostsRepositoryImpl implements PostsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Posts> findAll(PostsSearchDto searchDto) {
        return jpaQueryFactory
                .selectFrom(posts)
                .limit(searchDto.getSize())
                .offset(searchDto.getOffset())
                .where(posts.secret.eq(false))
                .orderBy(posts.id.desc())
                .fetch();
    }

    @Override
    public int findAllCount() {
        return jpaQueryFactory
                .selectFrom(posts)
                .where(posts.secret.eq(false))
                .fetch().size();
    }

    @Override
    public List<Posts> findAllByMember(Long memberId) {
        return jpaQueryFactory
                .selectFrom(posts)
                .where(posts.member.id.eq(memberId))
                .orderBy(posts.id.desc())
                .fetch();
    }

    @Override
    public List<Posts> findAllJoinWithHashTag(String name, HashTagSearchDto searchDto) {
        return jpaQueryFactory
                .selectFrom(posts)
                .leftJoin(postsHashTagMap)
                .on(posts.id.eq(postsHashTagMap.posts.id))
                .leftJoin(hashTag)
                .on(postsHashTagMap.hashTag.id.eq(hashTag.id))
                .limit(searchDto.getSize())
                .offset(searchDto.getOffset())
                .where(hashTag.name.eq(name))
                .orderBy(posts.id.desc())
                .fetch();
    }

    @Override
    public int findAllJoinWithHashTagCount(String name) {
        return jpaQueryFactory
                .selectFrom(posts)
                .leftJoin(postsHashTagMap)
                .on(posts.id.eq(postsHashTagMap.posts.id))
                .leftJoin(hashTag)
                .on(postsHashTagMap.hashTag.id.eq(hashTag.id))
                .where(hashTag.name.eq(name))
                .fetch().size();
    }

}
