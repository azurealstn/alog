package com.azurealstn.alog.domain.image;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostsImage is a Querydsl query type for PostsImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostsImage extends EntityPathBase<PostsImage> {

    private static final long serialVersionUID = 2090561992L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostsImage postsImage = new QPostsImage("postsImage");

    public final com.azurealstn.alog.domain.QBaseTimeEntity _super = new com.azurealstn.alog.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath originalFilename = createString("originalFilename");

    public final com.azurealstn.alog.domain.posts.QPosts posts;

    public final StringPath storeFilename = createString("storeFilename");

    public final BooleanPath thumbnailYn = createBoolean("thumbnailYn");

    public QPostsImage(String variable) {
        this(PostsImage.class, forVariable(variable), INITS);
    }

    public QPostsImage(Path<? extends PostsImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostsImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostsImage(PathMetadata metadata, PathInits inits) {
        this(PostsImage.class, metadata, inits);
    }

    public QPostsImage(Class<? extends PostsImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.posts = inits.isInitialized("posts") ? new com.azurealstn.alog.domain.posts.QPosts(forProperty("posts"), inits.get("posts")) : null;
    }

}

