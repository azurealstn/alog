package com.azurealstn.alog.dto.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
public class ValidationDto {

    private final String fieldName;
    private final String errorMessage;

    @Builder
    public ValidationDto(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }
}
