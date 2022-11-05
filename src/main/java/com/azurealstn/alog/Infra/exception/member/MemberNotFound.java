package com.azurealstn.alog.Infra.exception.member;

import com.azurealstn.alog.Infra.exception.GlobalException;

/**
 * Member 테이블에서 이메일 조회 -> null
 * status -> 404
 */
public class MemberNotFound extends GlobalException {

    private static String MESSAGE = "해당 회원이 없습니다.";

    public MemberNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
