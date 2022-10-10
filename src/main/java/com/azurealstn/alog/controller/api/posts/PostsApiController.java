package com.azurealstn.alog.controller.api.posts;

import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsModifyRequestDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
import com.azurealstn.alog.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<PostsResponseDto> posts(@ModelAttribute PostsSearchDto searchDto) {
        return postsService.findAll(searchDto);
    }

    @PutMapping("/api/v1/posts/{postsId}")
    public Long posts(@PathVariable Long postsId, @Valid @RequestBody PostsModifyRequestDto requestDto) {
        return postsService.modify(postsId, requestDto);
    }

    @DeleteMapping("/api/v1/posts/{postsId}")
    public void posts(@PathVariable Long postsId) {
        postsService.delete(postsId);
    }
}
