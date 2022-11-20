package com.azurealstn.alog.controller.api.hashtag;

import com.azurealstn.alog.dto.hashtag.PostsHashTagRequestDto;
import com.azurealstn.alog.service.hashtag.PostsHashTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostsHashTagApiController {

    private final PostsHashTagService postsHashTagService;

    @PostMapping("/api/v1/posts-hash-tag")
    public void create(@RequestBody PostsHashTagRequestDto requestDto) {
        postsHashTagService.create(requestDto);
    }

    @DeleteMapping("/api/v1/posts-hash-tag/{postsId}")
    public void delete(@PathVariable Long postsId) {
        postsHashTagService.delete(postsId);
    }
}
