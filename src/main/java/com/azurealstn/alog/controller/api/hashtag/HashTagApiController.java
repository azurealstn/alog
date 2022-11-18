package com.azurealstn.alog.controller.api.hashtag;

import com.azurealstn.alog.dto.hashtag.HashTagRequestDto;
import com.azurealstn.alog.service.hashtag.HashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HashTagApiController {

    private final HashTagService hashTagService;

    @PostMapping("/api/v1/hashtag")
    public void hashtag(@RequestBody HashTagRequestDto requestDto) {
        hashTagService.create(requestDto);
    }
}
