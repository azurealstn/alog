package com.azurealstn.alog.repository.image;

import com.azurealstn.alog.domain.image.PostsImage;
import com.azurealstn.alog.domain.image.QPostsImage;
import com.azurealstn.alog.domain.posts.QPosts;
import com.azurealstn.alog.repository.hashtag.PostsHashTagMapRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.azurealstn.alog.domain.hashtag.QPostsHashTagMap.postsHashTagMap;
import static com.azurealstn.alog.domain.image.QPostsImage.*;
import static com.azurealstn.alog.domain.posts.QPosts.*;

@RequiredArgsConstructor
public class PostsImageRepositoryImpl implements PostsImageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Optional<PostsImage> findThumbnailByPosts(Long postsId) {
        PostsImage postsImage = jpaQueryFactory
                .selectFrom(QPostsImage.postsImage)
                .leftJoin(posts)
                .on(QPostsImage.postsImage.posts.id.eq(posts.id))
                .where(QPostsImage.postsImage.thumbnailYn.eq(true)
                        .and(posts.id.eq(postsId)))
                .fetchOne();

        return Optional.ofNullable(postsImage);
    }

    @Override
    public void deleteByThumbnail(Long postsId) {
        jpaQueryFactory
                .delete(postsImage)
                .where(postsImage.thumbnailYn.eq(true)
                        .and(postsImage.posts.id.eq(postsId)))
                .execute();
    }
}
