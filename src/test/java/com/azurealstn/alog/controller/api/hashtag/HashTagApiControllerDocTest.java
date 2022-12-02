package com.azurealstn.alog.controller.api.hashtag;

import com.azurealstn.alog.Infra.exception.hashtag.HashTagNotFound;
import com.azurealstn.alog.Infra.exception.posts.PostsNotFound;
import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.domain.hashtag.PostsHashTagMap;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.hashtag.HashTagCreateRequestDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsModifyRequestDto;
import com.azurealstn.alog.repository.hashtag.HashTagRepository;
import com.azurealstn.alog.repository.hashtag.PostsHashTagMapRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.repository.posts.PostsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.alog.com", uriPort = 443)
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
public class HashTagApiControllerDocTest {

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

    @Autowired
    private HashTagRepository hashTagRepository;

    @Autowired
    private PostsHashTagMapRepository postsHashTagMapRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        postsHashTagMapRepository.deleteAll();
        hashTagRepository.deleteAll();
        postsRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        postsHashTagMapRepository.deleteAll();
        hashTagRepository.deleteAll();
        postsRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("해시태그 여러건 조회")
    @WithMockUser("MEMBER")
    @Transactional
    void findAll_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .secret(false)
                .description("소개글")
                .build();

        Long savedId = postsRepository.save(posts).getId();
        Posts foundPosts = postsRepository.findById(savedId)
                .orElseThrow(() -> new PostsNotFound());

        String tagName1 = "AWS 공부";
        String tagName2 = "Java 공부";
        HashTagCreateRequestDto requestDto1 = HashTagCreateRequestDto.builder()
                .name(tagName1)
                .build();

        HashTagCreateRequestDto requestDto2 = HashTagCreateRequestDto.builder()
                .name(tagName2)
                .build();

        Long hashTagId1 = hashTagRepository.save(requestDto1.toEntity()).getId();
        Long hashTagId2 = hashTagRepository.save(requestDto2.toEntity()).getId();

        HashTag hashTag1 = hashTagRepository.findById(hashTagId1)
                .orElseThrow(() -> new HashTagNotFound());
        HashTag hashTag2 = hashTagRepository.findById(hashTagId2)
                .orElseThrow(() -> new HashTagNotFound());

        PostsHashTagMap postsHashTagMap1 = PostsHashTagMap.builder()
                .posts(foundPosts)
                .hashTag(hashTag1)
                .build();
        PostsHashTagMap postsHashTagMap2 = PostsHashTagMap.builder()
                .posts(foundPosts)
                .hashTag(hashTag2)
                .build();

        postsHashTagMapRepository.save(postsHashTagMap1);
        postsHashTagMapRepository.save(postsHashTagMap2);

        mockMvc.perform(get("/api/v1/hashtag/{postsId}", savedId)
                        .session(mockHttpSession)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("hashtag-findAll", pathParameters(
                                parameterWithName("postsId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].name").description("해시태그 이름"),
                                fieldWithPath("[].posts.id").description("게시글 ID"),
                                fieldWithPath("[].posts.createdDate").description("게시글 생성 날짜"),
                                fieldWithPath("[].posts.modifiedDate").description("게시글 수정 날짜"),
                                fieldWithPath("[].posts.title").description("게시글 제목"),
                                fieldWithPath("[].posts.content").description("게시글 내용"),
                                fieldWithPath("[].posts.description").description("게시글 한 줄 소개"),
                                fieldWithPath("[].posts.secret").description("게시글 비밀글"),
                                fieldWithPath("[].posts.likes").description("게시글 좋아요"),
                                fieldWithPath("[].posts.member.createdDate").description("회원 생성 날짜"),
                                fieldWithPath("[].posts.member.modifiedDate").description("회원 수정"),
                                fieldWithPath("[].posts.member.id").description("회원 ID"),
                                fieldWithPath("[].posts.member.email").description("회원 이메일"),
                                fieldWithPath("[].posts.member.name").description("회원 이름"),
                                fieldWithPath("[].posts.member.picture").description("회원 프로필 사진"),
                                fieldWithPath("[].posts.member.role").description("회원 권한"),
                                fieldWithPath("[].posts.member.emailAuth").description("회원 이메일 인증"),
                                fieldWithPath("[].posts.member.username").description("회원 아이디"),
                                fieldWithPath("[].posts.member.shortBio").description("회원 소개"),
                                fieldWithPath("[].posts.member.roleKey").description("회원 권한 키")
                        )
                ));
    }

    @Test
    @DisplayName("해시태그 추가")
    @WithMockUser("MEMBER")
    @Transactional
    void create_hashtag_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        String tagName = "AWS 공부";
        HashTagCreateRequestDto requestDto = HashTagCreateRequestDto.builder()
                .name(tagName)
                .build();

        //expected
        mockMvc.perform(post("/api/v1/hashtag")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("hashtag-create",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("해시태그 이름")
                        )
                ));

    }

    private MemberCreateRequestDto getMemberCreateRequestDto() {
        String name = "슬로우스타터";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "안녕하세요!";
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
