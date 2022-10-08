package com.azurealstn.alog.controller.api.posts;

import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long posts(@Valid @RequestBody PostsCreateRequestDto requestDto) throws Exception {
        return postsService.create(requestDto);
    }
}
