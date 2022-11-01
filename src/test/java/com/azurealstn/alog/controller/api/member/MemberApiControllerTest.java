package com.azurealstn.alog.controller.api.member;

import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser("MEMBER")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberApiControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() throws Exception {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 등록 API")
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
                .andExpect(content().string("1"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 등록시 회원 이름 빈값")
    void create_member_name_is_null() throws Exception {
        //given
        String name = "   ";
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

        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "클라이언트의 잘못된 요청이 있습니다. (application/json)";

        //expected
        mockMvc.perform(post("/api/v1/auth/create-member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation[0].fieldName").value("name"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("이름을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 등록시 회원 이름 길이")
    void create_member_name_max() throws Exception {
        //given
        String name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
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

        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "클라이언트의 잘못된 요청이 있습니다. (application/json)";

        //expected
        mockMvc.perform(post("/api/v1/auth/create-member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation[0].fieldName").value("name"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("이름은 최대 45자까지 입력 할 수 있습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 등록시 회원 email wrong format")
    void create_member_email_wrong_format() throws Exception {
        //given
        String name = "슬로우스타터";
        String email = "azurealstn";
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

        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "클라이언트의 잘못된 요청이 있습니다. (application/json)";

        //expected
        mockMvc.perform(post("/api/v1/auth/create-member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation[0].fieldName").value("email"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("잘못된 이메일 형식입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 등록시 회원 username wrong format")
    void create_member_username_wrong_format() throws Exception {
        //given
        String name = "슬로우스타터";
        String email = "azurealstn@naver.com";
        String username = "12@$#%@!";
        String shortBio = "안녕하세요!";
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
        String message = "클라이언트의 잘못된 요청이 있습니다. (application/json)";

        //expected
        mockMvc.perform(post("/api/v1/auth/create-member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation[0].fieldName").value("username"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("아이디는 3~16자의 알파벳,숫자,혹은 - _ 으로 이루어져야 합니다."))
                .andDo(print());
    }
}