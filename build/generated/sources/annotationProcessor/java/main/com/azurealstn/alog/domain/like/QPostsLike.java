package com.azurealstn.alog.domain.like;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostsLike is a Querydsl query type for PostsLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostsLike extends EntityPathBase<PostsLike> {

    private static final long serialVersionUID = 1687827840L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostsLike postsLike = new QPostsLike("postsLike");

    public final com.azurealstn.alog.domain.QBaseTimeEntity _super = new com.azurealstn.alog.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.azurealstn.alog.domain.member.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final com.azurealstn.alog.domain.posts.QPosts posts;

    public QPostsLike(String variable) {
        this(PostsLike.class, forVariable(variable), INITS);
    }

    public QPostsLike(Path<? extends PostsLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostsLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostsLike(PathMetadata metadata, PathInits inits) {
        this(PostsLike.class, metadata, inits);
    }

    public QPostsLike(Class<? extends PostsLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.azurealstn.alog.domain.member.QMember(forProperty("member")) : null;
        this.posts = inits.isInitialized("posts") ? new com.azurealstn.alog.domain.posts.QPosts(forProperty("posts"), inits.get("posts")) : null;
    }

}

