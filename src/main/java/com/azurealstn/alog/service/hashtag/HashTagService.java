package com.azurealstn.alog.service.hashtag;

import com.azurealstn.alog.dto.hashtag.HashTagRequestDto;
import com.azurealstn.alog.repository.hashtag.HashTagRepository;
import com.azurealstn.alog.repository.hashtag.PostsHashTagMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HashTagService {

    private final HashTagRepository hashTagRepository;

    /**
     * 해쉬태그 저장
     */
    @Transactional
    public void create(HashTagRequestDto requestDto) {
        hashTagRepository.save(requestDto.toEntity());
    }
}
