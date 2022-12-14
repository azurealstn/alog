package com.azurealstn.alog.controller.api.member;

import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.member.Role;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.dto.member.MemberModifyRequestDto;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser("MEMBER")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberApiControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        memberRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("?????? ?????? API")
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
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? ??????")
    void create_member_name_is_null() throws Exception {
        //given
        String name = "   ";
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

        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "?????????????????? ????????? ????????? ????????????. (application/json)";

        //expected
        mockMvc.perform(post("/api/v1/auth/create-member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation[0].fieldName").value("name"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? ??????")
    void create_member_name_max() throws Exception {
        //given
        String name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
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

        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "?????????????????? ????????? ????????? ????????????. (application/json)";

        //expected
        mockMvc.perform(post("/api/v1/auth/create-member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation[0].fieldName").value("name"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("????????? ?????? 45????????? ?????? ??? ??? ????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ????????? ?????? email wrong format")
    void create_member_email_wrong_format() throws Exception {
        //given
        String name = "??????????????????";
        String email = "azurealstn";
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

        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "?????????????????? ????????? ????????? ????????????. (application/json)";

        //expected
        mockMvc.perform(post("/api/v1/auth/create-member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation[0].fieldName").value("email"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("????????? ????????? ???????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ????????? ?????? username wrong format")
    void create_member_username_wrong_format() throws Exception {
        //given
        String name = "??????????????????";
        String email = "azurealstn@naver.com";
        String username = "12@$#%@!";
        String shortBio = "???????????????!";
        String picture = "test.jpg";

        MemberCreateRequestDto requestDto = MemberCreateRequestDto.builder()
                .name(name)
                .email(email)
                .username(username)
                .shortBio(shortBio)
                .picture(picture)
                .build();

        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "?????????????????? ????????? ????????? ????????????. (application/json)";

        //expected
        mockMvc.perform(post("/api/v1/auth/create-member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation[0].fieldName").value("username"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("???????????? 3~16?????? ?????????,??????,?????? - _ ?????? ??????????????? ?????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/v1/member/{memberId} ????????? ??? ??? ?????? ??????")
    @Transactional(readOnly = true)
    void findById_o() throws Exception {
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
                .andExpect(jsonPath("$.id").value(savedId))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.email", is(email)))
                .andExpect(jsonPath("$.username", is(username)))
                .andExpect(jsonPath("$.shortBio", is(shortBio)))
                .andExpect(jsonPath("$.picture", is(picture)))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/v1/member/{memberId} ????????? ??? ??? ?????? ??????")
    @Transactional(readOnly = true)
    void findById_x() throws Exception {
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
        mockMvc.perform(get("/api/v1/member/{memberId}", savedId + 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("?????? name ????????? ??????")
    @Transactional
    void modify_member_o() throws Exception {
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
                .andDo(print());
    }

    @Test
    @DisplayName("?????? name ????????? ??????")
    @Transactional
    void modify_member_x() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember1 = memberRepository.save(memberCreateRequestDto.toEntity());

        String name = "????????? ??????";
        String email = "azurealstn123@naver.com";
        String username = "haha123";
        String shortBio = "???????????????!";
        String picture = "test.jpg";

        MemberCreateRequestDto createRequestDto = MemberCreateRequestDto.builder()
                .name(name)
                .email(email)
                .username(username)
                .shortBio(shortBio)
                .picture(picture)
                .build();

        Member savedMember2 = memberRepository.save(createRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember2));

        String expectedName = "??????????????????";
        String expectedShortBio = "????????? ??????";

        MemberModifyRequestDto requestDto = MemberModifyRequestDto.builder()
                .name(expectedName)
                .shortBio(expectedShortBio)
                .build();

        //expected
        mockMvc.perform(patch("/api/v1/member-name/{memberId}", savedMember2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("400")))
                .andExpect(jsonPath("$.message", is("?????? ???????????? ???????????????.")))
                .andExpect(jsonPath("$.validation.length()", is(0)))
                .andExpect(jsonPath("$.validation").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("?????? username ????????? ??????")
    @Transactional
    void modify_member_username_o() throws Exception {
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
                .andDo(print());
    }

    @Test
    @DisplayName("?????? username ????????? ??????")
    @Transactional
    void modify_member_username_x() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember1 = memberRepository.save(memberCreateRequestDto.toEntity());

        String name = "????????? ??????";
        String email = "azurealstn123@naver.com";
        String username = "haha123";
        String shortBio = "???????????????!";
        String picture = "test.jpg";

        MemberCreateRequestDto createRequestDto = MemberCreateRequestDto.builder()
                .name(name)
                .email(email)
                .username(username)
                .shortBio(shortBio)
                .picture(picture)
                .build();

        Member savedMember2 = memberRepository.save(createRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember2));

        String expectedUsername = "haha";

        MemberModifyRequestDto requestDto = MemberModifyRequestDto.builder()
                .username(expectedUsername)
                .build();

        //expected
        mockMvc.perform(patch("/api/v1/member-username/{memberId}", savedMember2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("400")))
                .andExpect(jsonPath("$.message", is("?????? ???????????? ??????????????????.")))
                .andExpect(jsonPath("$.validation.length()", is(0)))
                .andExpect(jsonPath("$.validation").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void member_delete_o() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        //expected
        mockMvc.perform(delete("/api/v1/member/{memberId}", savedMember.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void member_delete_x() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        //expected
        mockMvc.perform(delete("/api/v1/member/{memberId}", savedMember.getId() + 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
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