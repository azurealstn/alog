package com.azurealstn.alog.service.login;

import com.azurealstn.alog.Infra.exception.member.MemberNotFound;
import com.azurealstn.alog.domain.email.EmailAuth;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.dto.login.LoginRequestDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.repository.email.EmailAuthRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class LoginServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @Autowired
    private LoginService loginService;

    @BeforeEach
    void beforeEach() throws Exception {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("login 가상 테스트")
    @Transactional
    void login_virtual_test() {
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

        memberRepository.save(requestDto.toEntity());

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email(email)
                .build();

        //when
        boolean existsByEmail = memberRepository.existsByEmail(requestDto.getEmail());
        EmailAuth emailAuth = loginService.createEmailAuth(loginRequestDto);

        //then
        assertThat(existsByEmail).isTrue();
        assertThat(emailAuth.getExpired()).isFalse();
    }

    @Test
    @DisplayName("email로 회원 조회하기")
    @Transactional
    void find_by_email() throws Exception {
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

        memberRepository.save(requestDto.toEntity());

        //when
        Member findMember = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new MemberNotFound());

        //then
        assertThat(findMember.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("email로 회원 조회하기 실패")
    @Transactional
    void find_by_email_fail() throws Exception {
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

        memberRepository.save(requestDto.toEntity());

        //when
        String findEmail = "azurealsnt@naver.com";
        Optional<Member> member = memberRepository.findByEmail(findEmail);

        //then
        assertThat(member).isEmpty();

    }
}