package com.azurealstn.alog.controller.api.member;

import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.member.Role;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.dto.member.MemberModifyRequestDto;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsModifyRequestDto;
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
public class MemberApiControllerDocTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RestDocumentationContextProvider restDocumentationContextProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        memberRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 단건 조회")
    @WithMockUser("MEMBER")
    @Transactional
    void findById_api() throws Exception {
        //given
        String name = "슬로우스타터";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "안녕하세요!";
        String picture = "test.jpg";

        Member member = Member.builder()
                .name(name)
                .email(email)
                .username(username)
                .shortBio(shortBio)
                .picture(picture)
                .role(Role.MEMBER)
                .build();

        Long savedId = memberRepository.save(member).getId();

        //expected
        mockMvc.perform(get("/api/v1/member/{memberId}", savedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-findById", pathParameters(
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("picture").type(JsonFieldType.STRING).description("회원 프로필사진"),
                                fieldWithPath("role").type(JsonFieldType.STRING).description("회원 권한"),
                                fieldWithPath("emailAuth").type(JsonFieldType.NULL).description("회원 인증 상태"),
                                fieldWithPath("username").type(JsonFieldType.STRING).description("회원 아이디"),
                                fieldWithPath("shortBio").type(JsonFieldType.STRING).description("회원 한 줄 소개"),
                                fieldWithPath("role").type(JsonFieldType.STRING).description("회원 권한"),
                                fieldWithPath("existsEmail").type(JsonFieldType.NULL).description("회원 이메일 존재 여부")
                        )
                ));
    }

    @Test
    @DisplayName("회원 생성")
    @WithMockUser("MEMBER")
    @Transactional
    void create_member_api() throws Exception {
        //given
        String name = "슬로우스타터";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "안녕하세요!";
        String picture = "test.jpg";

        MemberCreateRequestDto requestDto = MemberCreateRequestDto.builder()
                .name(name)
                .email(email)
                .username(username)
                .shortBio(shortBio)
                .picture(picture)
                .build();

        //expected
        mockMvc.perform(post("/api/v1/auth/create-member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-create",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                                fieldWithPath("username").type(JsonFieldType.STRING).description("회원 아이디"),
                                fieldWithPath("shortBio").type(JsonFieldType.STRING).description("회원 소개"),
                                fieldWithPath("picture").type(JsonFieldType.STRING).description("회원 프로필 사진"),
                                fieldWithPath("role").type(JsonFieldType.NULL).description("회원 권한"),
                                fieldWithPath("emailAuth").type(JsonFieldType.NULL).description("회원 인증 상태")

                        )
                ));

    }

    @Test
    @DisplayName("회원 이름 수정")
    @WithMockUser("MEMBER")
    @Transactional
    void modify_member_name_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        String expectedName = "새로운 이름";
        String expectedShortBio = "새로운 소개";

        MemberModifyRequestDto requestDto = MemberModifyRequestDto.builder()
                .name(expectedName)
                .shortBio(expectedShortBio)
                .build();

        //expected
        mockMvc.perform(patch("/api/v1/member-name/{memberId}", savedMember.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-modify-name",
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("shortBio").description("회원 소개"),
                                fieldWithPath("username").description("회원 아이디"),
                                fieldWithPath("picture").description("회원 프로필 사진")
                        )
                ));
    }

    @Test
    @DisplayName("회원 아이디 수정")
    @WithMockUser("MEMBER")
    @Transactional
    void modify_member_username_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        String expectedUsername = "abcde";

        MemberModifyRequestDto requestDto = MemberModifyRequestDto.builder()
                .username(expectedUsername)
                .build();

        //expected
        mockMvc.perform(patch("/api/v1/member-name/{memberId}", savedMember.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-modify-username",
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("shortBio").description("회원 소개"),
                                fieldWithPath("username").description("회원 아이디"),
                                fieldWithPath("picture").description("회원 프로필 사진")
                        )
                ));
    }

    @Test
    @DisplayName("회원 삭제")
    @WithMockUser("MEMBER")
    @Transactional
    void delete_member_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        //expected
        mockMvc.perform(delete("/api/v1/member/{memberId}", savedMember.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-delete",
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
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
