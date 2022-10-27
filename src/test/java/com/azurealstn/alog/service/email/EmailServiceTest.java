package com.azurealstn.alog.service.email;

import com.azurealstn.alog.domain.email.EmailAuth;
import com.azurealstn.alog.dto.login.LoginRequestDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.repository.email.EmailAuthRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.service.login.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EmailService emailService;

    @BeforeEach
    void beforeEach() throws Exception {
        emailAuthRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일 존재 여부에 따라 링크가 달라진다. (이메일 존재O)")
    void if_exists_email_return_different_link_O() throws Exception {
        //given
        String email = "azurealstn@naver.com";
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email(email)
                .build();

        String name = "슬로우스타터";
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

        //when
        EmailAuth emailAuth = loginService.createEmailAuth(loginRequestDto);
        boolean existsByEmail = memberRepository.existsByEmail(loginRequestDto.getEmail());
        SimpleMailMessage msg = new SimpleMailMessage();
        emailService.loginOrCreateMemberUrl(msg, existsByEmail, emailAuth.getEmail(), emailAuth.getAuthToken());

        String expectedLoginUrl = "http://localhost:8080/api/v1/auth/email-login?email=" + emailAuth.getEmail() + "&authToken=" + emailAuth.getAuthToken();

        //then
        assertThat(emailAuth.getEmail()).isEqualTo(email);
        assertThat(emailAuth.getAuthToken()).isNotEmpty();
        assertThat(msg.getText()).isEqualTo(expectedLoginUrl);
    }

    @Test
    @DisplayName("이메일 존재 여부에 따라 링크가 달라진다. (이메일 존재X)")
    void if_exists_email_return_different_link_X() throws Exception {
        //given
        String email = "azurealstn@naver.com";
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email(email)
                .build();

        //when
        EmailAuth emailAuth = loginService.createEmailAuth(loginRequestDto);
        boolean existsByEmail = memberRepository.existsByEmail(loginRequestDto.getEmail());
        SimpleMailMessage msg = new SimpleMailMessage();
        emailService.loginOrCreateMemberUrl(msg, existsByEmail, emailAuth.getEmail(), emailAuth.getAuthToken());

        String expectedCreateMemberUrl = "http://localhost:8080/api/v1/auth/create-member?email=" + emailAuth.getEmail() + "&authToken=" + emailAuth.getAuthToken();

        //then
        assertThat(emailAuth.getEmail()).isEqualTo(email);
        assertThat(emailAuth.getAuthToken()).isNotEmpty();
        assertThat(msg.getText()).isEqualTo(expectedCreateMemberUrl);
    }
}