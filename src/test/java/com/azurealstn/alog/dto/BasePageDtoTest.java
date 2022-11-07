package com.azurealstn.alog.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BasePageDtoTest {

    @Test
    @DisplayName("page calculate 테스트 첫번째")
    void page_calc_test_first() {
        //given
        int page = 1;
        int size = 12;
        int totalRowCount = 406;

        BasePageDto basePageDto = new BasePageDto(page, size, totalRowCount);

        //expected
        assertThat(basePageDto.getTotalPageCount()).isEqualTo(34);
        assertThat(basePageDto.getStartPage()).isEqualTo(1);
        assertThat(basePageDto.getEndPage()).isEqualTo(10);
        assertThat(basePageDto.isPrevPage()).isFalse();
        assertThat(basePageDto.isNextPage()).isTrue();
    }

    @Test
    @DisplayName("page calculate 테스트 두번째")
    void page_calc_test_second() {
        //given
        int page = 12;
        int size = 12;
        int totalRowCount = 406;

        BasePageDto basePageDto = new BasePageDto(page, size, totalRowCount);

        //expected
        assertThat(basePageDto.getTotalPageCount()).isEqualTo(34);
        assertThat(basePageDto.getStartPage()).isEqualTo(11);
        assertThat(basePageDto.getEndPage()).isEqualTo(20);
        assertThat(basePageDto.isPrevPage()).isTrue();
        assertThat(basePageDto.isNextPage()).isTrue();
    }

    @Test
    @DisplayName("page calculate 테스트 세번째")
    void page_calc_test_third() {
        //given
        int page = 34;
        int size = 12;
        int totalRowCount = 406;

        BasePageDto basePageDto = new BasePageDto(page, size, totalRowCount);

        //expected
        assertThat(basePageDto.getTotalPageCount()).isEqualTo(34);
        assertThat(basePageDto.getStartPage()).isEqualTo(31);
        assertThat(basePageDto.getEndPage()).isEqualTo(34);
        assertThat(basePageDto.isPrevPage()).isTrue();
        assertThat(basePageDto.isNextPage()).isFalse();
    }
}