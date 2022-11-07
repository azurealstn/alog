package com.azurealstn.alog.dto.posts;

import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
public class PostsResponseDto implements Serializable {
    private final Long id;
    private final String title;
    private final String content;
    private final Member member;
    private final String description;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.member = entity.getMember();
        this.description = entity.getDescription();
    }
}
