package com.azurealstn.alog.Infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * EmailAuth 테이블에서 authToken으로 조회
 * status -> 410
 */
@ResponseStatus(value = HttpStatus.GONE)
public class EmailAuthTokenNotFountException extends RuntimeException {

    private static String MESSAGE = "인증 가능한 토큰이 없습니다.";

    public EmailAuthTokenNotFountException() {
        super(MESSAGE);
    }

//    @Override
//    public int getStatusCode() {
//        return 410;
//    }
}
