package com.azurealstn.alog.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PaginationDto {

    private List<Integer> pagination = new ArrayList<>();
    private boolean hasDoublePrevPage;
    private boolean hasDoubleNextPage;
    private int doublePrevPage;
    private int doubleNextPage;
    private int movePrevPage;
    private int moveNextPage;

    public PaginationDto(int startPage, int endPage, int page, int totalPageCount) {
        for (int i = startPage; i <= endPage; i++) {
            this.pagination.add(i);
        }

        this.hasDoublePrevPage = (page / 10) > 0;
        this.hasDoubleNextPage = (page / 10) < (totalPageCount / 10);
        this.doublePrevPage = startPage - 10;
        this.doubleNextPage = startPage + 10;
        this.movePrevPage = page - 1;
        this.moveNextPage = page + 1;
    }
}
