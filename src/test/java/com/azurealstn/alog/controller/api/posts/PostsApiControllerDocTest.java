package com.azurealstn.alog.controller.api.posts;

import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsModifyRequestDto;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.repository.posts.PostsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

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

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RestDocumentationContextProvider restDocumentationContextProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        postsRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        postsRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("??? ?????? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void findById_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .description("?????????")
                .secret(true)
                .build();

        //when
        Long savedId = postsRepository.save(posts).getId();

        mockMvc.perform(get("/api/v1/auth/posts-data/{posts_id}", savedId)
                        .session(mockHttpSession)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("posts-findById", pathParameters(
                                parameterWithName("posts_id").description("????????? ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("????????? ID"),
                                fieldWithPath("title").description("????????? ??????"),
                                fieldWithPath("content").description("????????? ??????"),
                                fieldWithPath("description").description("????????? ??? ??? ??????"),
                                fieldWithPath("secret").description("?????????"),
                                fieldWithPath("previousTime").description("????????? ????????? ?????? ??????"),
                                fieldWithPath("hashTagNames").description("???????????? ??????"),
                                fieldWithPath("likeCount").description("????????? ?????????"),
                                fieldWithPath("commentCount").description("?????? ?????????"),
                                fieldWithPath("storeFilename").description("?????? ?????? ?????? ??????"),
                                fieldWithPath("totalRowCount").description("?????? ????????? ???"),
                                fieldWithPath("imageUrl").type(JsonFieldType.NULL).description("????????? URL"),
                                fieldWithPath("member").type(JsonFieldType.OBJECT).description("?????? ??????"),
                                fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("member.email").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("member.name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("member.picture").type(JsonFieldType.STRING).description("?????? ???????????????"),
                                fieldWithPath("member.role").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("member.emailAuth").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                                fieldWithPath("member.username").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("member.shortBio").type(JsonFieldType.STRING).description("?????? ??? ??? ??????"),
                                fieldWithPath("member.roleKey").type(JsonFieldType.STRING).description("?????? ?????? ???"),
                                fieldWithPath("member.createdDate").type(JsonFieldType.STRING).description("?????? ????????????"),
                                fieldWithPath("member.modifiedDate").type(JsonFieldType.STRING).description("?????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("??? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void create_posts_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        String title = "??? ??????";
        String content = "??? ??????";
        String description = "??? ??????";

        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title(title)
                .content(content)
                .member(savedMember)
                .description(description)
                .secret(true)
                .build();

        //expected
        mockMvc.perform(post("/api/v1/posts")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("posts-create",
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("????????? ??? ??? ??????"),
                                fieldWithPath("secret").type(JsonFieldType.BOOLEAN).description("?????????"),
                                fieldWithPath("member").type(JsonFieldType.OBJECT).description("?????? ??????"),
                                fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("member.email").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("member.name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("member.picture").type(JsonFieldType.STRING).description("?????? ???????????????"),
                                fieldWithPath("member.role").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("member.emailAuth").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                                fieldWithPath("member.username").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("member.shortBio").type(JsonFieldType.STRING).description("?????? ??? ??? ??????"),
                                fieldWithPath("member.roleKey").type(JsonFieldType.STRING).description("?????? ?????? ???"),
                                fieldWithPath("member.createdDate").type(JsonFieldType.STRING).description("?????? ????????????"),
                                fieldWithPath("member.modifiedDate").type(JsonFieldType.STRING).description("?????? ????????????")

                        )
                ));

    }

    @Test
    @DisplayName("??? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void modify_posts_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .description("??????")
                .build();

        Long modifiedId = postsRepository.save(posts).getId();

        String expectedTitle = "????????? ??????";
        String expectedContent = "????????? ??????";
        String expectedDescription = "????????? ??????";

        PostsModifyRequestDto requestDto = PostsModifyRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .description(expectedDescription)
                .secret(true)
                .build();

        //expected
        mockMvc.perform(put("/api/v1/posts/{posts_id}", modifiedId)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("posts-modify",
                        pathParameters(
                                parameterWithName("posts_id").description("????????? ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("????????? ??????"),
                                fieldWithPath("content").description("????????? ??????"),
                                fieldWithPath("description").description("????????? ??? ??? ??????"),
                                fieldWithPath("secret").description("?????????")
                        )
                ));
    }

    @Test
    @DisplayName("??? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void delete_posts_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .description("??????")
                .build();

        Long deletedId = postsRepository.save(posts).getId();

        //expected
        mockMvc.perform(delete("/api/v1/posts/{posts_id}", deletedId)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("posts-delete",
                        pathParameters(
                                parameterWithName("posts_id").description("????????? ID")
                        )
                ));
    }

    private MemberCreateRequestDto getMemberCreateRequestDto() {
        String name = "??????????????????";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "???????????????!";
        String picture = "test.jpg";

        return MemberCreateRequestDto.builder()
                .name(name)
                .email(email)
                .username(username)
                .shortBio(shortBio)
                .picture(picture)
                .build();
    }
}
