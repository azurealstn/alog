package com.azurealstn.alog.Infra.utils;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class DateUtilsTest {

    @Test
    @DisplayName("previousTimeCalc 계산 확인 테스트")
    void previousTimeCalc() throws Exception {
        //given
        LocalDateTime seconds = LocalDateTime.now().minusSeconds(30); //30초
        LocalDateTime minutes = LocalDateTime.now().minusMinutes(30); //30분
        LocalDateTime hours = LocalDateTime.now().minusHours(22); //22시간
        LocalDateTime days = LocalDateTime.now().minusDays(3); //3일
        LocalDateTime nothing = LocalDateTime.of(2022, 10, 14, 9, 39, 7);

        assertThat(DateUtils.previousTimeCalc(seconds)).isEqualTo("방금 전");
        assertThat(DateUtils.previousTimeCalc(minutes)).isEqualTo("30분 전");
        assertThat(DateUtils.previousTimeCalc(hours)).isEqualTo("약 22시간 전");
        assertThat(DateUtils.previousTimeCalc(days)).isEqualTo("3일 전");
        assertThat(DateUtils.previousTimeCalc(nothing)).isEqualTo("2022년 10월 14일");
    }
}