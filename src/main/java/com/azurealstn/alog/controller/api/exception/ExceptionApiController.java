package com.azurealstn.alog.controller.api.exception;

import com.azurealstn.alog.Infra.exception.GlobalException;
import com.azurealstn.alog.dto.exception.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 스프링에서 제공하는 MethodArgumentNotValidException 예외 같은 경우는 각각 따로 만든다.
 * 이 안에서 처리해야 할 내용이 Exception 종류마다 다를 수 있다.
 * 하지만, 내부에서 정의해둔 규칙이 있다면 그것들은 공통 최상위 예외 추상화 클래스를 만들어서
 * 그 추상 클래스를 상속받도록 한 곳에서 처리한다.
 *
 */
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
        String message = "클라이언트의 잘못된 요청이 있습니다. (application/json)";
        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .code(code)
                .message(message)
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            responseDto.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return responseDto;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponseDto bindException(BindException e) {
        String code = "400";
        String message = "클라이언트의 잘못된 요청이 있습니다. (x-www-form-urlencoded)";
        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .code(code)
                .message(message)
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            responseDto.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return responseDto;
    }

    /**
     * 에러가 발생했을 때 이 클래스에서 정의한 예외가 아닌 다른 예외가 터지면 PostsNotFound 예외가 터진다.
     * 하지만 PostsNotFound 예외는 RuntimeException을 상속받았기 때문에 무조건 서버에러(500)를 발생시킨다.
     * 따라서 발생한 에러에 대한 정확한 HTTP 상태코드를 발생시켜줘야 한다.
     * @ResponseStatus 대신에 ResponseEntity 클래스를 응답받는다.
     */
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponseDto> globalException(GlobalException e) {
        int statusCode = e.getStatusCode();
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .build();

        ResponseEntity<ErrorResponseDto> responseEntity = ResponseEntity.status(statusCode).body(errorResponseDto);

        return responseEntity;
    }
}
