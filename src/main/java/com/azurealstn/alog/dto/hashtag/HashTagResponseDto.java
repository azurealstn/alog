package com.azurealstn.alog.dto.hashtag;

import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.member.Role;
import com.azurealstn.alog.domain.posts.Posts;
import lombok.Getter;

@Getter
public class HashTagResponseDto {

    private String name;
    private Posts posts;

    public HashTagResponseDto(HashTag entity) {
        this.name = entity.getName();
    }

}
