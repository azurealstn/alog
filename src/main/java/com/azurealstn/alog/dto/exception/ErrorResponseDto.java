package com.azurealstn.alog.dto.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
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
 *     @JsonInclude: 비어있는 값도 하나의 정보이기 때문에 팀마다 다를 수 있다.
 * }
 */
@Getter
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY) //비어있지 않은 JSON만 출력
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
