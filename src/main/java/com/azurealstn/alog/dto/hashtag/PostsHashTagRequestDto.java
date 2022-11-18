package com.azurealstn.alog.dto.hashtag;

import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.domain.hashtag.PostsHashTagMap;
import com.azurealstn.alog.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsHashTagRequestDto {

    private Posts posts;
    private HashTag hashTag;

    @Builder
    public PostsHashTagRequestDto(Posts posts, HashTag hashTag) {
        this.posts = posts;
        this.hashTag = hashTag;
    }

    public PostsHashTagMap toEntity() {
        return PostsHashTagMap.builder()
                .posts(posts)
                .hashTag(hashTag)
                .build();
    }
}
