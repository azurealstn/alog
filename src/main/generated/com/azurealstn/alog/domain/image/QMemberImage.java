package com.azurealstn.alog.domain.image;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberImage is a Querydsl query type for MemberImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberImage extends EntityPathBase<MemberImage> {

    private static final long serialVersionUID = 1698694753L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberImage memberImage = new QMemberImage("memberImage");

    public final com.azurealstn.alog.domain.QBaseTimeEntity _super = new com.azurealstn.alog.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final com.azurealstn.alog.domain.member.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath originalFilename = createString("originalFilename");

    public final StringPath storeFilename = createString("storeFilename");

    public final BooleanPath thumbnailYn = createBoolean("thumbnailYn");

    public QMemberImage(String variable) {
        this(MemberImage.class, forVariable(variable), INITS);
    }

    public QMemberImage(Path<? extends MemberImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberImage(PathMetadata metadata, PathInits inits) {
        this(MemberImage.class, metadata, inits);
    }

    public QMemberImage(Class<? extends MemberImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.azurealstn.alog.domain.member.QMember(forProperty("member")) : null;
    }

}

