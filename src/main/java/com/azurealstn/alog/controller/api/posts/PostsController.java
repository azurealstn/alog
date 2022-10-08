package com.azurealstn.alog.controller.api.posts;

import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class PostsController {

    @PostMapping("/posts")
    public Map<String, String> posts(@RequestBody PostsCreateRequestDto requestDto) throws Exception {
        log.info("title={}, content={}", requestDto.getTitle(), requestDto.getContent());

        /**
         * 아래 코드의 단점
         * 1. 필드가 추가될 때마다 if문을 계속 추가주어야 한다. (노가다)
         * 2. 개발팁) 무언가 3번이상 반복 작업을 하고 있다면 내가 뭔가 잘못하고 있는건 아닌지 의심한다.
         * 3. 개발자가 까먹고 누락할 가능성이 있다.
         * 4. 생각보다 검증해야할 것이 많다. (중요)
         *  - {"title": ""} -> 정상적인 에러 발생
         *  - {"title": "            "} -> 에러 발생 X
         *  - {"title": "..........수백만의 글자"} -> 에러 발생 X
         *  - 등등..
         */
        if (requestDto.getTitle() == null || requestDto.getTitle().equals("")) {
            throw new Exception("제목이 비어있습니다.");
        }

        if (requestDto.getContent() == null || requestDto.getContent().equals("")) {
            throw new Exception("내용이 비어있습니다.");
        }

        return Map.of();
    }
}
