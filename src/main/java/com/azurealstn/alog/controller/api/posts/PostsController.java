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
    public Map<String, String> posts(@Valid @RequestBody PostsCreateRequestDto requestDto) throws Exception {
        log.info("title={}, content={}", requestDto.getTitle(), requestDto.getContent());
        return Map.of();
    }
}
