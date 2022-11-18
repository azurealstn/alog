package com.azurealstn.alog.service.hashtag;

import com.azurealstn.alog.dto.hashtag.PostsHashTagRequestDto;
import com.azurealstn.alog.repository.hashtag.PostsHashTagMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsHashTagService {

    private final PostsHashTagMapRepository postsHashTagMapRepository;

    /**
     * Posts와 HashTag 테이블의 매칭 테이블 저장
     */
    @Transactional
    public void create(PostsHashTagRequestDto requestDto) {
        postsHashTagMapRepository.save(requestDto.toEntity());
    }
}
