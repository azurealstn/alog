package com.azurealstn.alog.Infra.exception.member;

import com.azurealstn.alog.Infra.exception.GlobalException;

public class AlreadyExistsName extends GlobalException {

    private static String MESSAGE = "이미 존재하는 이름입니다.";

    public AlreadyExistsName() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 400;
    }
}
