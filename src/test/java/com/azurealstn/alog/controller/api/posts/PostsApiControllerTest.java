package com.azurealstn.alog.controller.api.posts;

import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsModifyRequestDto;
import com.azurealstn.alog.repository.posts.PostsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @BeforeEach
    void beforeEach() throws Exception {
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
        String message = "클라이언트의 잘못된 요청이 있습니다. (application/json)";

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
    @DisplayName("/posts 게시글 등록시 DB 저장 mockmvc")
    void create_posts_mock() throws Exception {
        //given
        String title = "제목입니다.";
        String content = "내용입니다.";
        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title(title)
                .content(content)
                .build();

        //expected
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"))
                .andDo(print());

    }

    @Test
    @DisplayName("/api/v1/posts/{postsId} 요청시 글 단건 조회 성공")
    void select_posts_o() throws Exception {
        //given
        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .build();

        String expectedTitle = "foo";
        String expectedContent = "bar";

        //when
        Long savedId = postsRepository.save(posts).getId();

        //then
        mockMvc.perform(get("/api/v1/posts/{postsId}", savedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedId))
                .andExpect(jsonPath("$.title", is(expectedTitle)))
                .andExpect(jsonPath("$.content", is(expectedContent)))
                .andDo(print());

    }

    @Test
    @DisplayName("/api/v1/posts/{postsId} 요청시 글 단건 조회 실패")
    void select_posts_x() throws Exception {
        //given
        Long postsId = 1L;

        //expected
        mockMvc.perform(get("/api/v1/posts/{postsId}", postsId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("글 목록 첫번째 페이지 조회")
    void findAll_posts_first_page() throws Exception {
        //given
        List<Posts> collect = IntStream.range(0, 30)
                .mapToObj(i -> Posts.builder()
                        .title("test 제목 - " + (i + 1))
                        .content("뭐로 할까 - " + (i + 1))
                        .build())
                .collect(Collectors.toList());
        postsRepository.saveAll(collect);

        //expected
        mockMvc.perform(get("/api/v1/posts?page=1&size=9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(9)))
                .andExpect(jsonPath("$[0].title").value("test 제목 - 30"))
                .andExpect(jsonPath("$[0].content").value("뭐로 할까 - 30"))
                .andExpect(jsonPath("$[8].title").value("test 제목 - 22"))
                .andExpect(jsonPath("$[8].content").value("뭐로 할까 - 22"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 목록 두번째 페이지 조회")
    void findAll_posts_second_page() throws Exception {
        //given
        List<Posts> collect = IntStream.range(0, 30)
                .mapToObj(i -> Posts.builder()
                        .title("test 제목 - " + (i + 1))
                        .content("뭐로 할까 - " + (i + 1))
                        .build())
                .collect(Collectors.toList());
        postsRepository.saveAll(collect);

        //expected
        mockMvc.perform(get("/api/v1/posts?page=2&size=9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(9)))
                .andExpect(jsonPath("$[0].title").value("test 제목 - 21"))
                .andExpect(jsonPath("$[0].content").value("뭐로 할까 - 21"))
                .andExpect(jsonPath("$[8].title").value("test 제목 - 13"))
                .andExpect(jsonPath("$[8].content").value("뭐로 할까 - 13"))
                .andDo(print());

    }

    @Test
    @DisplayName("?page=1이 아닌 ?page=0으로 요청해도 첫 페이지를 가져온다.")
    void first_page_zero() throws Exception {
        //given
        List<Posts> collect = IntStream.range(0, 30)
                .mapToObj(i -> Posts.builder()
                        .title("test 제목 - " + (i + 1))
                        .content("뭐로 할까 - " + (i + 1))
                        .build())
                .collect(Collectors.toList());
        postsRepository.saveAll(collect);

        //expected
        mockMvc.perform(get("/api/v1/posts?page=0&size=9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(9)))
                .andExpect(jsonPath("$[0].title").value("test 제목 - 30"))
                .andExpect(jsonPath("$[0].content").value("뭐로 할까 - 30"))
                .andExpect(jsonPath("$[8].title").value("test 제목 - 22"))
                .andExpect(jsonPath("$[8].content").value("뭐로 할까 - 22"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정시 DB 수정 성공")
    void modify_posts_api_o() throws Exception {
        //given
        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .build();

        Long modifiedId = postsRepository.save(posts).getId();

        //when
        String expectedTitle = "제목입니다.";
        String expectedContent = "내용입니다.";
        PostsModifyRequestDto modifyRequestDto = PostsModifyRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + modifiedId;

        HttpEntity<PostsModifyRequestDto> httpEntity = new HttpEntity<>(modifyRequestDto);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Long.class);
        List<Posts> all = postsRepository.findAll();

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(postsRepository.count()).isEqualTo(1);
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

    @Test
    @DisplayName("게시글 수정시 DB 수정 성공 mockmvc")
    void modify_posts_api_o_mock() throws Exception {
        //given
        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .build();

        Long modifiedId = postsRepository.save(posts).getId();

        String expectedTitle = "제목입니다.";
        String expectedContent = "내용입니다.";
        PostsModifyRequestDto modifyRequestDto = PostsModifyRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        //expected
        mockMvc.perform(put("/api/v1/posts/{posts_id}", modifiedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정시 DB 수정 실패")
    void modify_posts_api_x() throws Exception {
        //given
        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .build();

        Long modifiedId = postsRepository.save(posts).getId();

        String expectedTitle = "제목입니다.";
        String expectedContent = "내용입니다.";
        PostsModifyRequestDto modifyRequestDto = PostsModifyRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        //expected
        mockMvc.perform(put("/api/v1/posts/{postsId}", modifiedId + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제시 DB 삭제")
    void delete_posts_api() {
        //given
        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .build();

        Long deletedId = postsRepository.save(posts).getId();

        String url = "http://localhost:" + port + "/api/v1/posts/" + deletedId;

        HttpEntity<Posts> httpEntity = new HttpEntity<>(posts);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, Long.class);
        List<Posts> all = postsRepository.findAll();

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(all.size()).isEqualTo(0);
        assertThat(postsRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 삭제시 DB 삭제 mockmvc")
    void delete_posts_api_mock() throws Exception {
        //given
        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .build();

        Long deletedId = postsRepository.save(posts).getId();

        //expected
        mockMvc.perform(delete("/api/v1/posts/{posts_id}", deletedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제시 DB 삭제 실패")
    void delete_posts_api_x() throws Exception {
        //given
        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .build();

        Long deletedId = postsRepository.save(posts).getId();

        //expected
        mockMvc.perform(delete("/api/v1/posts/{postsId}", deletedId + 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}