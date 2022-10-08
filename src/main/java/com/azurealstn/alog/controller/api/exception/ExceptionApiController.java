package com.azurealstn.alog.controller.api.exception;

import com.azurealstn.alog.dto.exception.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionApiController {

    /**
     * MethodArgumentNotValidException
     * @RequestBody 애노테이션으로 받은 파라미터 -> JSON
     *
     * BindException.class
     * @ModelAttribute 애노테이션으로 받은 파라미터 -> KEY/VALUE
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDto methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String code = "400";
        String message = "잘못된 요청입니다.";
        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .code(code)
                .message(message)
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            responseDto.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return responseDto;
    }
}
