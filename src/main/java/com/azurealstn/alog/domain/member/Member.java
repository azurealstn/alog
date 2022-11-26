package com.azurealstn.alog.domain.member;

import com.azurealstn.alog.domain.BaseTimeEntity;
import com.azurealstn.alog.domain.comment.Comment;
import com.azurealstn.alog.domain.image.MemberImage;
import com.azurealstn.alog.domain.like.PostsLike;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.domain.tempsave.TempSave;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column
    private String name;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private Boolean emailAuth; //이메일 인증 상태

    @Column
    private String username; //블로그 아이디

    @Column
    private String shortBio; //한 줄 소개

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Posts> postsList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<TempSave> tempSaveList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<PostsLike> postsLikeList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<MemberImage> memberImageList = new ArrayList<>();

    @Builder
    public Member(String email, String name, String picture, Role role, Boolean emailAuth, String username, String shortBio) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.role = role;
        this.emailAuth = emailAuth;
        this.username = username;
        this.shortBio = shortBio;
    }

    //네이버 프로필 변경시 Member 테이블 업데이트
    public Member modify_name(String name, String shortBio) {
        this.name = name;
        this.shortBio = shortBio;
        return this;
    }

    public Member modify_username(String username) {
        this.username = username;
        return this;
    }

    public Member modify_picture(String picture) {
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    //이메일 인증을 진행해주는 메서드
    public void emailVerifiedSuccess() {
        this.emailAuth = true;
    }

}
