package com.azurealstn.alog.controller.api.posts;

import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsModifyRequestDto;
import com.azurealstn.alog.repository.posts.PostsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.alog.com", uriPort = 443)
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
public class PostsApiControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostsRepository postsRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    @DisplayName("글 단건 조회")
    void findById_api() throws Exception {
        //given
        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .build();

        //when
        Long savedId = postsRepository.save(posts).getId();

        mockMvc.perform(get("/api/v1/posts/{posts_id}", savedId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("posts-findById", pathParameters(
                                parameterWithName("posts_id").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용")
                        )
                ));
    }

    @Test
    @DisplayName("글 작성")
    void create_posts_api() throws Exception {
        //given
        String title = "글 제목";
        String content = "글 내용";

        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title(title)
                .content(content)
                .build();

        //expected
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("posts-create",
                        requestFields(
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용")
                        )
                ));

    }

    @Test
    @DisplayName("글 수정")
    void modify_posts_api() throws Exception {
        //given
        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .build();

        Long modifiedId = postsRepository.save(posts).getId();

        String expectedTitle = "수정된 제목";
        String expectedContent = "수정된 내용";

        PostsModifyRequestDto requestDto = PostsModifyRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        //expected
        mockMvc.perform(put("/api/v1/posts/{posts_id}", modifiedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("posts-modify",
                        pathParameters(
                                parameterWithName("posts_id").description("게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용")
                        )
                ));
    }

    @Test
    @DisplayName("글 삭제")
    void delete_posts_api() throws Exception {
        //given
        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .build();

        Long deletedId = postsRepository.save(posts).getId();

        //expected
        mockMvc.perform(delete("/api/v1/posts/{posts_id}", deletedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("posts-delete",
                        pathParameters(
                                parameterWithName("posts_id").description("게시글 ID")
                        )
                ));
    }
}
