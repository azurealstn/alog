package com.azurealstn.alog.repository.comment;

import com.azurealstn.alog.domain.comment.Comment;
import com.azurealstn.alog.domain.comment.QComment;
import com.azurealstn.alog.domain.email.EmailAuth;
import com.azurealstn.alog.repository.email.EmailAuthRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.azurealstn.alog.domain.comment.QComment.*;
import static com.azurealstn.alog.domain.email.QEmailAuth.emailAuth;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findAllCommentLevel0() {
        return jpaQueryFactory
                .selectFrom(comment)
                .leftJoin(comment)
                .on(comment.id.eq(comment.upCommentId))
                .where(comment.level.eq(0))
                .fetch();
    }

    @Override
    public List<Comment> findAllCommentLevel1(Long commentId) {
        return jpaQueryFactory
                .selectFrom(comment)
                .leftJoin(comment)
                .on(comment.id.eq(comment.upCommentId))
                .where(comment.level.eq(1)
                        .and(comment.upCommentId.eq(commentId)))
                .fetch();
    }

}
