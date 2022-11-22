package com.azurealstn.alog.domain.comment;

import com.azurealstn.alog.domain.BaseTimeEntity;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Lob
    @Column(length = 512)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @Column(name = "up_comment_id")
    private Long upCommentId;

    @Column
    private Integer level;

    @Column
    private Boolean isCommentMe;

    @Builder
    public Comment(Long id, String content, Member member, Posts posts, Long upCommentId, Integer level, Boolean isCommentMe) {
        this.id = id;
        this.content = content;
        this.member = member;
        this.posts = posts;
        this.upCommentId = upCommentId;
        this.level = level;
        this.isCommentMe = isCommentMe;
    }

    public void modifyContent(String content) {
        this.content = content;
    }
}
