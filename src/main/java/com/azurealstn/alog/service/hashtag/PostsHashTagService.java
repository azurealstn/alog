package com.azurealstn.alog.service.hashtag;

import com.azurealstn.alog.Infra.exception.hashtag.HashTagNotFound;
import com.azurealstn.alog.Infra.exception.posts.PostsNotFound;
import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.domain.hashtag.PostsHashTagMap;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.hashtag.PostsHashTagRequestDto;
import com.azurealstn.alog.repository.hashtag.HashTagRepository;
import com.azurealstn.alog.repository.hashtag.PostsHashTagMapRepository;
import com.azurealstn.alog.repository.posts.PostsRepository;
import com.azurealstn.alog.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsHashTagService {

    private final PostsHashTagMapRepository postsHashTagMapRepository;
    private final PostsRepository postsRepository;
    private final HashTagRepository hashTagRepository;

    /**
     * Posts와 HashTag 테이블의 매칭 테이블 저장
     */
    @Transactional
    public void create(PostsHashTagRequestDto requestDto) {
        Posts posts = postsRepository.findById(requestDto.getPostsId())
                .orElseThrow(() -> new PostsNotFound());
        HashTag hashTag = hashTagRepository.findById(requestDto.getHashtagId())
                .orElseThrow(() -> new HashTagNotFound());
        PostsHashTagMap postsHashTagMap = PostsHashTagMap.builder()
                .posts(posts)
                .hashTag(hashTag)
                .build();
        postsHashTagMapRepository.save(postsHashTagMap);
    }

    @Transactional
    public void delete(Long postsId) {
        postsHashTagMapRepository.deleteByPostsId(postsId);
    }

}
