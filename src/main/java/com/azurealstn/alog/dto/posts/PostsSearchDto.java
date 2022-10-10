package com.azurealstn.alog.dto.posts;

import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.*;

@Builder
@Getter
public class PostsSearchDto {

    private static final int MAX_SIZE = 200;

    @Builder.Default
    private int page = 1; //페이지 수

    @Builder.Default
    private int size = 10; //한 페이지당 수

    public long getOffset() {
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }
}
