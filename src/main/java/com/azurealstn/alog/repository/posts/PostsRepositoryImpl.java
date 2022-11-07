package com.azurealstn.alog.repository.posts;

import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.domain.posts.QPosts;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
                .orderBy(posts.id.desc())
                .fetch();
    }

    @Override
    public int findAllCount() {
        return jpaQueryFactory
                .selectFrom(posts)
                .fetch().size();
    }

}
