package com.azurealstn.alog.controller.login;

import com.azurealstn.alog.domain.email.EmailAuth;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.dto.login.LoginRequestDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.repository.email.EmailAuthRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.service.login.LoginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser("MEMBER")
@AutoConfigureMockMvc
@SpringBootTest
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoginService loginService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        memberRepository.deleteAll();
        emailAuthRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
        emailAuthRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일 로그인")
    void email_login() throws Exception {
        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email("azurealstn@naver.com")
                .build();

        EmailAuth emailAuth = loginService.createEmailAuth(loginRequestDto);
        emailAuthRepository.save(emailAuth);

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

        memberRepository.save(requestDto.toEntity());

        //expected
        mockMvc.perform(get("/api/v1/auth/email-login")
                        .param("email", emailAuth.getEmail())
                        .param("authToken", emailAuth.getAuthToken()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("SNS 로그인")
    void sns_login() throws Exception {
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

        Member savedMember = memberRepository.save(requestDto.toEntity());

        //expected
        mockMvc.perform(get("/api/v1/auth/snsLogin")
                        .param("email", savedMember.getEmail()))
                .andExpect(status().is3xxRedirection());
    }
}