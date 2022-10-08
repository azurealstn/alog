package com.azurealstn.alog.controller.api.exception;

import com.azurealstn.alog.dto.exception.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDto methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ErrorResponseDto responseDto = new ErrorResponseDto("400", "잘못된 요청입니다.");

        for (FieldError fieldError : e.getFieldErrors()) {
            responseDto.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return responseDto;
    }
}
