package com.azurealstn.alog.dto.posts;

import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class PostsCreateRequestDto {

    @NotBlank(message = "제목이 비어있습니다.")
    private String title;

    @NotBlank(message = "내용이 비어있습니다.")
    private String content;

    private Member member;

    @Size(max = 150, message = "포스트 소개 글을 150자 내로 적어주세요.")
    private String description;

    @Builder
    public PostsCreateRequestDto(String title, String content, Member member, String description) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.description = description;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .member(member)
                .description(description)
                .build();
    }

    public void updateMember(Member member) {
        this.member = member;
    }
}

