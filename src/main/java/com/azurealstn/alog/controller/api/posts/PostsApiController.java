package com.azurealstn.alog.controller.api.posts;

import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long posts(@Valid @RequestBody PostsCreateRequestDto requestDto) throws Exception {
        return postsService.create(requestDto);
    }

    @GetMapping("/api/v1/posts/{postsId}")
    public PostsResponseDto findById(@PathVariable Long postsId) {
        return postsService.findById(postsId);
    }

    @GetMapping("/api/v1/posts")
    public List<PostsResponseDto> posts(@PageableDefault(size = 9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postsService.findAll(pageable);
    }
}
