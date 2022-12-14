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
    @DisplayName("?????? ?????? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void findById_api() throws Exception {
        //given
        String name = "??????????????????";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "???????????????!";
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
                                parameterWithName("memberId").description("?????? ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("picture").type(JsonFieldType.STRING).description("?????? ???????????????"),
                                fieldWithPath("role").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("emailAuth").type(JsonFieldType.NULL).description("?????? ?????? ??????"),
                                fieldWithPath("username").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("shortBio").type(JsonFieldType.STRING).description("?????? ??? ??? ??????"),
                                fieldWithPath("role").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("existsEmail").type(JsonFieldType.NULL).description("?????? ????????? ?????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void create_member_api() throws Exception {
        //given
        String name = "??????????????????";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "???????????????!";
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
                                fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("username").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("shortBio").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("picture").type(JsonFieldType.STRING).description("?????? ????????? ??????"),
                                fieldWithPath("role").type(JsonFieldType.NULL).description("?????? ??????"),
                                fieldWithPath("emailAuth").type(JsonFieldType.NULL).description("?????? ?????? ??????")

                        )
                ));

    }

    @Test
    @DisplayName("?????? ?????? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void modify_member_name_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        String expectedName = "????????? ??????";
        String expectedShortBio = "????????? ??????";

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
                                parameterWithName("memberId").description("?????? ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("?????? ??????"),
                                fieldWithPath("shortBio").description("?????? ??????"),
                                fieldWithPath("username").description("?????? ?????????"),
                                fieldWithPath("picture").description("?????? ????????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ????????? ??????")
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
                                parameterWithName("memberId").description("?????? ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("?????? ??????"),
                                fieldWithPath("shortBio").description("?????? ??????"),
                                fieldWithPath("username").description("?????? ?????????"),
                                fieldWithPath("picture").description("?????? ????????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
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
                                parameterWithName("memberId").description("?????? ID")
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
