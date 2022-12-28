package com.azurealstn.alog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * page: 1
 * size: 10
 * totalRowCount: 36
 * ====================
 * tempEnd: 10, 20, 30, 40
 * startPage: 1, 11, 21, 31
 * endPage: 10, 20, 30, 36
 * ********************
 * tempEnd와 endPage의 차이점을 알아야한다!
 */
@ToString
@Getter
public class BasePageDto {

    private int startPage; //시작 페이지 번호
    private int endPage; //끝 페이지 번호
    private boolean prevPage; //이전 페이지
    private boolean nextPage; //다음 페이지
    private int totalRowCount; //전체 데이터 수
    private int totalPageCount; //전체 페이지 수

    public BasePageDto(Integer page, Integer size, int totalRowCount) {
        this.totalRowCount = totalRowCount;
        this.totalPageCount = ((totalRowCount - 1) / size) + 1;
        int tempEnd =  (int) Math.ceil(page / 10.0) * 10;
        this.startPage = tempEnd - 9;
        this.endPage = Math.min(totalPageCount, tempEnd);
        this.prevPage = page > 1;
        this.nextPage = totalPageCount > page;
    }

}
