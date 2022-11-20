package com.azurealstn.alog.domain.posts;

import com.azurealstn.alog.Infra.convert.BooleanToYNConverter;
import com.azurealstn.alog.domain.BaseTimeEntity;
import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.domain.hashtag.PostsHashTagMap;
import com.azurealstn.alog.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posts_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column
    private String description;

    @Column
    private Boolean secret;

    @JsonIgnore
    @OneToMany(mappedBy = "posts", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<PostsHashTagMap> postsHashTagMapList = new ArrayList<>();

    @Builder
    public Posts(String title, String content, Member member, String description, Boolean secret) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.description = description;
        this.secret = secret;
    }

    /**
     * setter를 사용하는 것보다는 수정되는 항목에 대해서만 정의하는 메서드를 만드는 것이 좋다. (명확하다.)
     */
    public void modify(String title, String content, String description, Boolean secret) {
        this.title = title;
        this.content = content;
        this.description = description;
        this.secret = secret;
    }
}
