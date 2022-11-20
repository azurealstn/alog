package com.azurealstn.alog.service.hashtag;

import com.azurealstn.alog.Infra.exception.hashtag.HashTagNotFound;
import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.dto.hashtag.HashTagCreateRequestDto;
import com.azurealstn.alog.dto.hashtag.HashTagModifyRequestDto;
import com.azurealstn.alog.dto.hashtag.HashTagResponseDto;
import com.azurealstn.alog.dto.hashtag.HashTagSearchDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
import com.azurealstn.alog.repository.hashtag.HashTagRepository;
import com.azurealstn.alog.repository.hashtag.PostsHashTagMapRepository;
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
        return hashTagList.stream()
                .map(hashTag -> new HashTagResponseDto(hashTag))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long postsId) {
        postsHashTagMapRepository.deleteByPostsId(postsId);
    }

    @Transactional(readOnly = true)
    public List<HashTagResponseDto> findByTagsName(String name, HashTagSearchDto searchDto) {
        List<HashTag> hashTagList = hashTagRepository.findByTagsName(name, searchDto);
        return hashTagList.stream()
                .map(hashTag -> new HashTagResponseDto(hashTag))
                .collect(Collectors.toList());
    }
}
