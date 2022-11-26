package com.azurealstn.alog.repository.image;

import com.azurealstn.alog.domain.image.MemberImage;
import com.azurealstn.alog.domain.image.PostsImage;
import com.azurealstn.alog.domain.image.QMemberImage;
import com.azurealstn.alog.domain.image.QPostsImage;
import com.azurealstn.alog.domain.member.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.azurealstn.alog.domain.image.QMemberImage.*;
import static com.azurealstn.alog.domain.image.QPostsImage.postsImage;
import static com.azurealstn.alog.domain.member.QMember.*;
import static com.azurealstn.alog.domain.posts.QPosts.posts;

@RequiredArgsConstructor
public class MemberImageRepositoryImpl implements MemberImageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Optional<MemberImage> findThumbnailByMember(Long memberId) {
        MemberImage memberImage = jpaQueryFactory
                .selectFrom(QMemberImage.memberImage)
                .leftJoin(member)
                .on(QMemberImage.memberImage.member.id.eq(memberId))
                .where(QMemberImage.memberImage.thumbnailYn.eq(true)
                        .and(QMemberImage.memberImage.member.id.eq(memberId)))
                .fetchOne();
        return Optional.ofNullable(memberImage);
    }

    @Override
    public void deleteByThumbnail(Long memberId) {
        jpaQueryFactory
                .delete(memberImage)
                .where(memberImage.thumbnailYn.eq(true)
                        .and(memberImage.member.id.eq(memberId)))
                .execute();
    }
}
