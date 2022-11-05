package com.azurealstn.alog.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PostsModifyRequestDto {

    @NotBlank(message = "제목이 비어있습니다.")
    private String title;

    @NotBlank(message = "내용이 비어있습니다.")
    private String content;

    private String description;

    @Builder
    public PostsModifyRequestDto(String title, String content, String description) {
        this.title = title;
        this.content = content;
        this.description = description;
    }
}
