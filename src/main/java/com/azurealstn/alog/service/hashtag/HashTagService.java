package com.azurealstn.alog.service.hashtag;

import com.azurealstn.alog.Infra.exception.hashtag.HashTagNotFound;
import com.azurealstn.alog.Infra.exception.posts.PostsNotFound;
import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.hashtag.HashTagCreateRequestDto;
import com.azurealstn.alog.dto.hashtag.HashTagModifyRequestDto;
import com.azurealstn.alog.dto.hashtag.HashTagResponseDto;
import com.azurealstn.alog.dto.hashtag.HashTagSearchDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
import com.azurealstn.alog.repository.hashtag.HashTagRepository;
import com.azurealstn.alog.repository.hashtag.PostsHashTagMapRepository;
import com.azurealstn.alog.repository.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HashTagService {

    private final HashTagRepository hashTagRepository;
    private final PostsHashTagMapRepository postsHashTagMapRepository;
    private final PostsRepository postsRepository;

    /**
     * 해쉬태그 저장
     */
    @Transactional
    public Long create(HashTagCreateRequestDto requestDto) {
        return hashTagRepository.save(requestDto.toEntity()).getId();
    }

    /**
     * 해쉬태그 조회
     */
    @Transactional(readOnly = true)
    public List<HashTagResponseDto> findByTags(Long postsId) {
        List<HashTag> hashTagList = hashTagRepository.findByTags(postsId);
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new PostsNotFound());
        return hashTagList.stream()
                .map(hashTag -> new HashTagResponseDto(hashTag, posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long postsId) {
        postsHashTagMapRepository.deleteByPostsId(postsId);
    }
}
