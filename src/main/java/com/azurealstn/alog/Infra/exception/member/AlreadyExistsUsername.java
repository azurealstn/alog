package com.azurealstn.alog.Infra.exception.member;

import com.azurealstn.alog.Infra.exception.GlobalException;

public class AlreadyExistsUsername extends GlobalException {

    private static String MESSAGE = "이미 존재하는 아이디입니다.";

    public AlreadyExistsUsername() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 400;
    }
}
