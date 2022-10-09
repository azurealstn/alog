package com.azurealstn.alog.controller.api.posts;

import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.repository.posts.PostsRepository;
import com.azurealstn.alog.service.posts.PostsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private PostsService postsService;

    @BeforeEach
    public void beforeEach() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 게시글 등록시 데이터 검증")
    void posts_create_api_validate() throws Exception {
        //given
        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title("")
                .content("")
                .build();
        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "잘못된 요청입니다.";

        //expected
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation.length()", is(2)))
                .andExpect(jsonPath("$.validation").isNotEmpty())
                .andExpect(jsonPath("$.validation[0].fieldName").exists())
                .andExpect(jsonPath("$.validation[0].errorMessage").exists())
                .andExpect(jsonPath("$.validation[1].fieldName").exists())
                .andExpect(jsonPath("$.validation[1].errorMessage").exists())
                .andDo(print());

    }

    @Test
    @DisplayName("/posts 게시글 등록시 DB 저장")
    void create_posts() throws Exception {
        //given
        String title = "제목입니다.";
        String content = "내용입니다.";
        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title(title)
                .content(content)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        List<Posts> all = postsRepository.findAll();

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(postsRepository.count()).isEqualTo(1);
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("/api/v1/posts/{postsId} 요청시 글 단건 조회")
    void select_posts() throws Exception {
        //given
        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title("foo")
                .content("bar")
                .build();

        //when
        Long savedId = postsService.create(requestDto);

        //then
        mockMvc.perform(get("/api/v1/posts/{postsId}", savedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedId))
                .andExpect(jsonPath("$.title", is(requestDto.getTitle())))
                .andExpect(jsonPath("$.content", is(requestDto.getContent())))
                .andDo(print());

    }

    @Test
    @DisplayName("/api/v1/posts/{postsId} 요청시 글 단건 조회")
    void findAll_posts() throws Exception {
        //given
        PostsCreateRequestDto requestDto1 = PostsCreateRequestDto.builder()
                .title("foo1")
                .content("bar1")
                .build();

        PostsCreateRequestDto requestDto2 = PostsCreateRequestDto.builder()
                .title("foo2")
                .content("bar2")
                .build();

        //when
        postsService.create(requestDto1);
        postsService.create(requestDto2);

        //then
        mockMvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("foo1"))
                .andExpect(jsonPath("$[0].content").value("bar1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("foo2"))
                .andExpect(jsonPath("$[1].content").value("bar2"))
                .andDo(print());

    }
}