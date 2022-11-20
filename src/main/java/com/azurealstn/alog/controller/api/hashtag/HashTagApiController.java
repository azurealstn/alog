package com.azurealstn.alog.controller.api.hashtag;

import com.azurealstn.alog.dto.hashtag.HashTagCreateRequestDto;
import com.azurealstn.alog.dto.hashtag.HashTagModifyRequestDto;
import com.azurealstn.alog.dto.hashtag.HashTagResponseDto;
import com.azurealstn.alog.service.hashtag.HashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class HashTagApiController {

    private final HashTagService hashTagService;

    @PostMapping("/api/v1/hashtag")
    public Long create(@RequestBody HashTagCreateRequestDto requestDto) {
        return hashTagService.create(requestDto);
    }

    @GetMapping("/api/v1/hashtag/{postsId}")
    public List<HashTagResponseDto> findByTags(@PathVariable Long postsId) {
        return hashTagService.findByTags(postsId);
    }
}
