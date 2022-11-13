package com.azurealstn.alog.controller.member;

import com.azurealstn.alog.domain.email.EmailAuth;
import com.azurealstn.alog.dto.email.EmailAuthRequestDto;
import com.azurealstn.alog.dto.login.LoginRequestDto;
import com.azurealstn.alog.repository.email.EmailAuthRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.service.login.LoginService;
import com.azurealstn.alog.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser("MEMBER")
@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        memberRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("/api/v1/auth/create-member status 200")
    void create_member_status_200() throws Exception {
        //given
        LoginRequestDto requestDto = LoginRequestDto.builder()
                .email("azurealstn@naver.com")
                .build();

        EmailAuth emailAuth = loginService.createEmailAuth(requestDto);

        EmailAuthRequestDto emailAuthRequestDto = EmailAuthRequestDto.builder()
                .email(emailAuth.getEmail())
                .authToken(emailAuth.getAuthToken())
                .expired(emailAuth.getExpired())
                .expiredDate(LocalDateTime.now())
                .build();

        emailAuthRepository.save(emailAuthRequestDto.toEntity());

        //expected
        mockMvc.perform(get("/api/v1/auth/create-member")
                        .param("email", emailAuthRequestDto.getEmail())
                        .param("authToken", emailAuthRequestDto.getAuthToken())
                        .param("expired", String.valueOf(emailAuthRequestDto.getExpired()))
                        .param("expiredDate", emailAuthRequestDto.getExpiredDate().toString()))
                .andExpect(status().isOk());
    }

}