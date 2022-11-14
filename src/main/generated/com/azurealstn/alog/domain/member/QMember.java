package com.azurealstn.alog.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1345294785L;

    public static final QMember member = new QMember("member1");

    public final com.azurealstn.alog.domain.QBaseTimeEntity _super = new com.azurealstn.alog.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final BooleanPath emailAuth = createBoolean("emailAuth");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    public final StringPath picture = createString("picture");

    public final ListPath<com.azurealstn.alog.domain.posts.Posts, com.azurealstn.alog.domain.posts.QPosts> postsList = this.<com.azurealstn.alog.domain.posts.Posts, com.azurealstn.alog.domain.posts.QPosts>createList("postsList", com.azurealstn.alog.domain.posts.Posts.class, com.azurealstn.alog.domain.posts.QPosts.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final StringPath shortBio = createString("shortBio");

    public final ListPath<com.azurealstn.alog.domain.tempsave.TempSave, com.azurealstn.alog.domain.tempsave.QTempSave> tempSaveList = this.<com.azurealstn.alog.domain.tempsave.TempSave, com.azurealstn.alog.domain.tempsave.QTempSave>createList("tempSaveList", com.azurealstn.alog.domain.tempsave.TempSave.class, com.azurealstn.alog.domain.tempsave.QTempSave.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

