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

    private Long postsId;
    private Long hashtagId;

    @Builder
    public PostsHashTagRequestDto(Long postsId, Long hashtagId) {
        this.postsId = postsId;
        this.hashtagId = hashtagId;
    }

}
