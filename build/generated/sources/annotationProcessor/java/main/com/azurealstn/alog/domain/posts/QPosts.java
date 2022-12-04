package com.azurealstn.alog.domain.posts;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPosts is a Querydsl query type for Posts
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPosts extends EntityPathBase<Posts> {

    private static final long serialVersionUID = -1439794229L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPosts posts = new QPosts("posts");

    public final com.azurealstn.alog.domain.QBaseTimeEntity _super = new com.azurealstn.alog.domain.QBaseTimeEntity(this);

    public final ListPath<com.azurealstn.alog.domain.comment.Comment, com.azurealstn.alog.domain.comment.QComment> commentList = this.<com.azurealstn.alog.domain.comment.Comment, com.azurealstn.alog.domain.comment.QComment>createList("commentList", com.azurealstn.alog.domain.comment.Comment.class, com.azurealstn.alog.domain.comment.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> likes = createNumber("likes", Integer.class);

    public final com.azurealstn.alog.domain.member.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final ListPath<com.azurealstn.alog.domain.hashtag.PostsHashTagMap, com.azurealstn.alog.domain.hashtag.QPostsHashTagMap> postsHashTagMapList = this.<com.azurealstn.alog.domain.hashtag.PostsHashTagMap, com.azurealstn.alog.domain.hashtag.QPostsHashTagMap>createList("postsHashTagMapList", com.azurealstn.alog.domain.hashtag.PostsHashTagMap.class, com.azurealstn.alog.domain.hashtag.QPostsHashTagMap.class, PathInits.DIRECT2);

    public final ListPath<com.azurealstn.alog.domain.image.PostsImage, com.azurealstn.alog.domain.image.QPostsImage> postsImageList = this.<com.azurealstn.alog.domain.image.PostsImage, com.azurealstn.alog.domain.image.QPostsImage>createList("postsImageList", com.azurealstn.alog.domain.image.PostsImage.class, com.azurealstn.alog.domain.image.QPostsImage.class, PathInits.DIRECT2);

    public final ListPath<com.azurealstn.alog.domain.like.PostsLike, com.azurealstn.alog.domain.like.QPostsLike> postsLikeList = this.<com.azurealstn.alog.domain.like.PostsLike, com.azurealstn.alog.domain.like.QPostsLike>createList("postsLikeList", com.azurealstn.alog.domain.like.PostsLike.class, com.azurealstn.alog.domain.like.QPostsLike.class, PathInits.DIRECT2);

    public final BooleanPath secret = createBoolean("secret");

    public final StringPath title = createString("title");

    public QPosts(String variable) {
        this(Posts.class, forVariable(variable), INITS);
    }

    public QPosts(Path<? extends Posts> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPosts(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPosts(PathMetadata metadata, PathInits inits) {
        this(Posts.class, metadata, inits);
    }

    public QPosts(Class<? extends Posts> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.azurealstn.alog.domain.member.QMember(forProperty("member")) : null;
    }

}

