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
    public Long create(PostsCreateRequestDto requestDto) {
        //Case 1. 저장한 데이터 Entity → response 응답
        //Case 2. 저장한 데이터의 primary_id → response 응답
        //  - Client에서는 수신한 id를 posts 조회 API를 통해서 글 데이터를 수신받음
        //Case 3. 응답 필요 없음 -> 클라이언트에서 모든 POST(글) 데이터 context를 잘 관리함
        Posts posts = requestDto.toEntity();
        postsRepository.save(posts);
        return posts.getId();
    }
}
