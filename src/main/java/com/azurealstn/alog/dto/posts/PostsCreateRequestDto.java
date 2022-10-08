package com.azurealstn.alog.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsCreateRequestDto {

    private String title;

    private String content;

    @Builder
    public PostsCreateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

