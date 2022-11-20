package com.azurealstn.alog.dto.like;

import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class PostsLikeRequestDto {

    @NotNull(message = "로그인 후 이용해주세요.")
    private Long memberId;
    private Long postsId;

    public PostsLikeRequestDto(Long memberId, Long postsId) {
        this.memberId = memberId;
        this.postsId = postsId;
    }
}

