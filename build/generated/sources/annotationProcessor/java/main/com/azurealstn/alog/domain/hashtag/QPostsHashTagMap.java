package com.azurealstn.alog.domain.hashtag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostsHashTagMap is a Querydsl query type for PostsHashTagMap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostsHashTagMap extends EntityPathBase<PostsHashTagMap> {

    private static final long serialVersionUID = -1631650444L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostsHashTagMap postsHashTagMap = new QPostsHashTagMap("postsHashTagMap");

    public final com.azurealstn.alog.domain.QBaseTimeEntity _super = new com.azurealstn.alog.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QHashTag hashTag;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final com.azurealstn.alog.domain.posts.QPosts posts;

    public QPostsHashTagMap(String variable) {
        this(PostsHashTagMap.class, forVariable(variable), INITS);
    }

    public QPostsHashTagMap(Path<? extends PostsHashTagMap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostsHashTagMap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostsHashTagMap(PathMetadata metadata, PathInits inits) {
        this(PostsHashTagMap.class, metadata, inits);
    }

    public QPostsHashTagMap(Class<? extends PostsHashTagMap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hashTag = inits.isInitialized("hashTag") ? new QHashTag(forProperty("hashTag")) : null;
        this.posts = inits.isInitialized("posts") ? new com.azurealstn.alog.domain.posts.QPosts(forProperty("posts"), inits.get("posts")) : null;
    }

}

