package com.azurealstn.alog.service.posts;

import com.azurealstn.alog.domain.Posts;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.repository.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    /**
     * 게시글 등록 API
     */
    public void create(PostsCreateRequestDto requestDto) {
        Posts posts = requestDto.toEntity();
        postsRepository.save(posts);
    }
}
