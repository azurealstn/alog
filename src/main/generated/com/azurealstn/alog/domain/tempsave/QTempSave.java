package com.azurealstn.alog.domain.tempsave;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTempSave is a Querydsl query type for TempSave
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTempSave extends EntityPathBase<TempSave> {

    private static final long serialVersionUID = 833316161L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTempSave tempSave = new QTempSave("tempSave");

    public final com.azurealstn.alog.domain.QBaseTimeEntity _super = new com.azurealstn.alog.domain.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.azurealstn.alog.domain.member.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath tempCode = createString("tempCode");

    public final StringPath title = createString("title");

    public QTempSave(String variable) {
        this(TempSave.class, forVariable(variable), INITS);
    }

    public QTempSave(Path<? extends TempSave> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTempSave(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTempSave(PathMetadata metadata, PathInits inits) {
        this(TempSave.class, metadata, inits);
    }

    public QTempSave(Class<? extends TempSave> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.azurealstn.alog.domain.member.QMember(forProperty("member")) : null;
    }

}

