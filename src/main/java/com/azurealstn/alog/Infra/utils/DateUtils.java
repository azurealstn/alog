package com.azurealstn.alog.Infra.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    public static String previousTimeCalc(LocalDateTime createdDate) {
        LocalDateTime now = LocalDateTime.now();

        long result = createdDate.until(now, ChronoUnit.SECONDS);

        if (result > 0 && result < 60) {
            return "방금 전";
        } else if (result >= 60 && result < 3600) {
            return (result / 60) + "분 전";
        } else if (result >= 3600 && result < 86400) {
            return "약 " + (result / 60 / 60) + "시간 전";
        } else if (result >= 86400 && result < 604800) {
            return (result / 60 / 60 / 24) + "일 전";
        } else {
            return createdDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 d일"));
        }

    }

}
