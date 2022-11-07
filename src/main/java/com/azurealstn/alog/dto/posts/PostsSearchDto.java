package com.azurealstn.alog.dto.posts;

import com.azurealstn.alog.dto.BasePageDto;
import lombok.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

@ToString
@Getter
public class PostsSearchDto {

    private static final int MAX_SIZE = 200;

    private Integer page; //현재 페이지 번호
    private Integer size; //한 페이지당 데이터 수
    private BasePageDto basePageDto;

    public PostsSearchDto(Integer page, Integer size) {
        this.page = (page == null) ? 1 : page;
        this.size = (size == null) ? 12 : size;
    }

    public long getOffset() {
        return (long) (max(1, this.page) - 1) * min(this.size, MAX_SIZE);
    }

    public void setBasePageDto(BasePageDto basePageDto) {
        this.basePageDto = basePageDto;
    }
}
