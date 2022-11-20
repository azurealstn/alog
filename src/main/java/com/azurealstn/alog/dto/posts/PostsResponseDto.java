package com.azurealstn.alog.dto.posts;

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
public class PostsResponseDto implements Serializable {
    private final Long id;
    private final String title;
    private final String content;
    private final Member member;
    private final String description;
    private final Boolean secret;
    private final String previousTime;
    private final List<HashTag> hashTagNames = new ArrayList<>();
    private int likeCount;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.member = entity.getMember();
        this.description = entity.getDescription();
        this.secret = entity.getSecret();
        this.previousTime = DateUtils.previousTimeCalc(entity.getCreatedDate());
    }

    public void addHashTag(HashTag hashTag) {
        this.hashTagNames.add(hashTag);
    }

    public void addLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

}
