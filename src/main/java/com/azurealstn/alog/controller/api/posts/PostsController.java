package com.azurealstn.alog.controller.api.posts;

import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PostsController {

    @PostMapping("/posts")
    public Map<String, String> posts(@Valid @RequestBody PostsCreateRequestDto requestDto, BindingResult result) throws Exception {
        log.info("title={}, content={}", requestDto.getTitle(), requestDto.getContent());

        /**
         * BindingResult의 단점
         * 1. 매번 매서드마다 값을 검증해야 한다.
         *  -> 개발자가 까먹을 수도 있고, 반복되는 작업은 힘들다.
         *  -> 검증 부분에서 버그가 발생할 여지가 높다.
         *  -> 세 번이상의 반복적인 작업은 피해야 한다.
         * 2. 응답값에 HashMap이 아닌 응답 클래스를 만들어주는 것이 좋다.
         * 3. 현재는 get(0)으로 한 개만 가져오지만 여러 개의 에러를 처리해야 할 경우에 힘들다.
         */
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError fieldError = fieldErrors.get(0);
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();

            Map<String, String> response = new HashMap<>();
            response.put(fieldName, errorMessage);
            return response;
        }

        return Map.of();
    }
}
