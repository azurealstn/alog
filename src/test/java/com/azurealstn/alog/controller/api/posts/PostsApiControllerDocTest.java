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
    @DisplayName("글 단건 조회")
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
                .description("소개글")
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
                                parameterWithName("posts_id").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("description").description("게시글 한 줄 소개"),
                                fieldWithPath("secret").description("비밀글"),
                                fieldWithPath("previousTime").description("게시글 올리고 지난 시간"),
                                fieldWithPath("hashTagNames").description("해시태그 이름"),
                                fieldWithPath("likeCount").description("좋아요 카운트"),
                                fieldWithPath("commentCount").description("댓글 카운트"),
                                fieldWithPath("storeFilename").description("서버 저장 파일 이름"),
                                fieldWithPath("totalRowCount").description("전체 게시글 수"),
                                fieldWithPath("imageUrl").type(JsonFieldType.NULL).description("이미지 URL"),
                                fieldWithPath("member").type(JsonFieldType.OBJECT).description("회원 정보"),
                                fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("member.email").type(JsonFieldType.STRING).description("회원 이메일"),
                                fieldWithPath("member.name").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("member.picture").type(JsonFieldType.STRING).description("회원 프로필사진"),
                                fieldWithPath("member.role").type(JsonFieldType.STRING).description("회원 권한"),
                                fieldWithPath("member.emailAuth").type(JsonFieldType.BOOLEAN).description("회원 인증 상태"),
                                fieldWithPath("member.username").type(JsonFieldType.STRING).description("회원 아이디"),
                                fieldWithPath("member.shortBio").type(JsonFieldType.STRING).description("회원 한 줄 소개"),
                                fieldWithPath("member.roleKey").type(JsonFieldType.STRING).description("회원 권한 키"),
                                fieldWithPath("member.createdDate").type(JsonFieldType.STRING).description("회원 생성날짜"),
                                fieldWithPath("member.modifiedDate").type(JsonFieldType.STRING).description("회원 수정날짜")
                        )
                ));
    }

    @Test
    @DisplayName("글 작성")
    @WithMockUser("MEMBER")
    @Transactional
    void create_posts_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        String title = "글 제목";
        String content = "글 내용";
        String description = "글 소개";

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
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("게시글 한 줄 소개"),
                                fieldWithPath("secret").type(JsonFieldType.BOOLEAN).description("비밀글"),
                                fieldWithPath("member").type(JsonFieldType.OBJECT).description("회원 정보"),
                                fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("member.email").type(JsonFieldType.STRING).description("회원 이메일"),
                                fieldWithPath("member.name").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("member.picture").type(JsonFieldType.STRING).description("회원 프로필사진"),
                                fieldWithPath("member.role").type(JsonFieldType.STRING).description("회원 권한"),
                                fieldWithPath("member.emailAuth").type(JsonFieldType.BOOLEAN).description("회원 인증 상태"),
                                fieldWithPath("member.username").type(JsonFieldType.STRING).description("회원 아이디"),
                                fieldWithPath("member.shortBio").type(JsonFieldType.STRING).description("회원 한 줄 소개"),
                                fieldWithPath("member.roleKey").type(JsonFieldType.STRING).description("회원 권한 키"),
                                fieldWithPath("member.createdDate").type(JsonFieldType.STRING).description("회원 생성날짜"),
                                fieldWithPath("member.modifiedDate").type(JsonFieldType.STRING).description("회원 수정날짜")

                        )
                ));

    }

    @Test
    @DisplayName("글 수정")
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
                .description("소개")
                .build();

        Long modifiedId = postsRepository.save(posts).getId();

        String expectedTitle = "수정된 제목";
        String expectedContent = "수정된 내용";
        String expectedDescription = "수정된 소개";

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
                                parameterWithName("posts_id").description("게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("description").description("게시글 한 줄 소개"),
                                fieldWithPath("secret").description("비밀글")
                        )
                ));
    }

    @Test
    @DisplayName("글 삭제")
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
                .description("소개")
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
                                parameterWithName("posts_id").description("게시글 ID")
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
