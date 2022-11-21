package com.azurealstn.alog.dto.comment;

import com.azurealstn.alog.domain.comment.Comment;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.member.Role;
import com.azurealstn.alog.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CommentCreateRequestDto {

    @NotBlank(message = "댓글이 비어있습니다.")
    @Size(max = 500, message = "댓글은 150자 내로 적어주세요.")
    private String content;

    private Member member;

    private Posts posts;

    private Long upCommentId;

    private int level;

    private Long memberId;
    private Long postsId;

    @Builder
    public CommentCreateRequestDto(String content, Member member, Posts posts, Long upCommentId, int level) {
        this.content = content;
        this.member = member;
        this.posts = posts;
        this.upCommentId = upCommentId;
        this.level = level;
    }

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .member(member)
                .posts(posts)
                .upCommentId(upCommentId)
                .level(level)
                .build();
    }

    public void updateMemberAndPosts(Member member, Posts posts) {
        this.member = member;
        this.posts = posts;
    }
}
