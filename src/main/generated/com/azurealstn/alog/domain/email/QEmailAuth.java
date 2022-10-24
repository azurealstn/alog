package com.azurealstn.alog.domain.email;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmailAuth is a Querydsl query type for EmailAuth
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailAuth extends EntityPathBase<EmailAuth> {

    private static final long serialVersionUID = -310961179L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmailAuth emailAuth = new QEmailAuth("emailAuth");

    public final com.azurealstn.alog.domain.QBaseTimeEntity _super = new com.azurealstn.alog.domain.QBaseTimeEntity(this);

    public final StringPath authToken = createString("authToken");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final BooleanPath expired = createBoolean("expired");

    public final DateTimePath<java.time.LocalDateTime> expiredDate = createDateTime("expiredDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.azurealstn.alog.domain.member.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public QEmailAuth(String variable) {
        this(EmailAuth.class, forVariable(variable), INITS);
    }

    public QEmailAuth(Path<? extends EmailAuth> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmailAuth(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmailAuth(PathMetadata metadata, PathInits inits) {
        this(EmailAuth.class, metadata, inits);
    }

    public QEmailAuth(Class<? extends EmailAuth> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.azurealstn.alog.domain.member.QMember(forProperty("member")) : null;
    }

}

