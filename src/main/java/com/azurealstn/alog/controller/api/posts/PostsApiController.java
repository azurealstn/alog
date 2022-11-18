package com.azurealstn.alog.controller.api.posts;

import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsModifyRequestDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
import com.azurealstn.alog.repository.posts.PostsRepository;
import com.azurealstn.alog.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @PostMapping("/api/v1/posts")
    public Long create(@Valid @RequestBody PostsCreateRequestDto requestDto) throws Exception {
        return postsService.create(requestDto);
    }

    @GetMapping("/api/v1/auth/posts-data/{postsId}")
    public PostsResponseDto findById(@PathVariable Long postsId) {
        return postsService.findById(postsId);
    }

    @GetMapping("/api/v1/posts")
    public List<PostsResponseDto> posts(@ModelAttribute PostsSearchDto searchDto) {
        return postsService.findAll(searchDto);
    }

    @PutMapping("/api/v1/posts/{postsId}")
    public Long modify(@PathVariable Long postsId, @Valid @RequestBody PostsModifyRequestDto requestDto) {
        return postsService.modify(postsId, requestDto);
    }

    @DeleteMapping("/api/v1/posts/{postsId}")
    public void posts(@PathVariable Long postsId) {
        postsService.delete(postsId);
    }

    @GetMapping("/api/v1/posts/by-member")
    public List<PostsResponseDto> postsListByMember() {
        SessionMemberDto member = (SessionMemberDto) httpSession.getAttribute("member");
        return postsService.findAllByMember(member.getId());
    }
}
