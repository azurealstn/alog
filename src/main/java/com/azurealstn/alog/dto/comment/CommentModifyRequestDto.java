package com.azurealstn.alog.dto.comment;

import com.azurealstn.alog.domain.comment.Comment;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CommentModifyRequestDto {

    @NotBlank(message = "댓글이 비어있습니다.")
    @Size(max = 500, message = "댓글은 150자 내로 적어주세요.")
    private String content;

    @Builder
    public CommentModifyRequestDto(String content) {
        this.content = content;
    }
}
