package com.azurealstn.alog.controller.api.like;

import com.azurealstn.alog.dto.like.PostsLikeRequestDto;
import com.azurealstn.alog.service.like.PostsLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class PostsLikeApiController {

    private final PostsLikeService postsLikeService;

    @PostMapping("/api/v1/auth/toggle-like")
    public Boolean toggleLikeButton(@Valid @RequestBody PostsLikeRequestDto requestDto) {
        return postsLikeService.toggleLikeButton(requestDto);
    }
}
