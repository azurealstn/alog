package com.azurealstn.alog.dto.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

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
public class ErrorResponseDto {

    private final String code;
    private final String message;
    private final List<ValidationDto> validation = new ArrayList<>();

    @Builder
    public ErrorResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addValidation(String fieldName, String errorMessage) {
        ValidationDto validDto = ValidationDto.builder()
                .fieldName(fieldName)
                .errorMessage(errorMessage)
                .build();
        this.validation.add(validDto);
    }
}
