package com.azurealstn.alog.dto.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {
 *     "code": "400",
 *     "message": "잘못된 요청입니다.",
 *     "validation": {
 *         "title": "제목이 비어있습니다.",
 *         "content": "내용이 비어있습니다."
 *     }
 * }
 */
@Getter
@RequiredArgsConstructor
public class ErrorResponseDto {

    private final String code;
    private final String message;
    private final List<ValidationDto> validation = new ArrayList<>();

    public void addValidation(String fieldName, String errorMessage) {
        ValidationDto validDto = ValidationDto.builder()
                .fieldName(fieldName)
                .errorMessage(errorMessage)
                .build();
        this.validation.add(validDto);
    }
}
