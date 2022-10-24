package com.azurealstn.alog.Infra.exception;

import java.util.ArrayList;
import java.util.List;

public abstract class GlobalException extends RuntimeException {

    public GlobalException(String message) {
        super(message);
    }

    public abstract int getStatusCode();

}
