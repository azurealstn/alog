package com.azurealstn.alog.controller.api.hashtag;

import com.azurealstn.alog.dto.hashtag.PostsHashTagRequestDto;
import com.azurealstn.alog.service.hashtag.PostsHashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostsHashTagApiController {

    private final PostsHashTagService postsHashTagService;

    @PostMapping("/api/v1/posts-hash-tag")
    public void posts_hash_tag(@RequestBody PostsHashTagRequestDto requestDto) {
        postsHashTagService.create(requestDto);
    }
}
