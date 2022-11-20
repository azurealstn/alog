package com.azurealstn.alog.dto.like;

import com.azurealstn.alog.Infra.utils.DateUtils;
import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class PostsLikeResponseDto implements Serializable {

    private int postsLikeCount;
    private boolean exist;

    public PostsLikeResponseDto(int postsLikeCount, boolean exist) {
        this.postsLikeCount = postsLikeCount;
        this.exist = exist;
    }
}
