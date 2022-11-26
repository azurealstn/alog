package com.azurealstn.alog.domain.like;

import com.azurealstn.alog.domain.BaseTimeEntity;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PostsLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @Builder
    public PostsLike(Member member, Posts posts) {
        this.member = member;
        this.posts = posts;
    }
}
