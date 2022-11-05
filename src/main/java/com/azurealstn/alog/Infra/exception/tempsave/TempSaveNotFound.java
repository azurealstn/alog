package com.azurealstn.alog.Infra.exception.tempsave;

import com.azurealstn.alog.Infra.exception.GlobalException;

public class TempSaveNotFound extends GlobalException {

    private static final String MESSAGE = "해당 임시글이 없습니다.";

    public TempSaveNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
