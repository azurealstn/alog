package com.azurealstn.alog.dto.hashtag;

import com.azurealstn.alog.domain.hashtag.HashTag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HashTagRequestDto {

    private String name;

    @Builder
    public HashTagRequestDto(String name) {
        this.name = name;
    }

    public HashTag toEntity() {
        return HashTag.builder()
                .name(name)
                .build();
    }
}
