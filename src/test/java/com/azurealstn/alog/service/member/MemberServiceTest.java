package com.azurealstn.alog.service.member;

import com.azurealstn.alog.Infra.exception.member.AlreadyExistsUsername;
import com.azurealstn.alog.Infra.exception.member.MemberNotFound;
import com.azurealstn.alog.domain.email.EmailAuth;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.member.Role;
import com.azurealstn.alog.dto.login.LoginRequestDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.dto.member.MemberResponseDto;
import com.azurealstn.alog.repository.email.EmailAuthRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.service.login.LoginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@WithMockUser("MEMBER")
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @BeforeEach
    public void beforeEach() throws Exception {
        memberRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    @Transactional
    void create_member() {
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

        //when
        Member member = requestDto.toEntity();
        Member savedMember = memberRepository.save(member);
        List<Member> all = memberRepository.findAll();

        //then
        assertThat(memberRepository.count()).isEqualTo(1L);
        assertThat(all.size()).isEqualTo(1);
        assertThat(savedMember.getName()).isEqualTo(name);
        assertThat(savedMember.getEmail()).isEqualTo(email);
        assertThat(savedMember.getUsername()).isEqualTo(username);
        assertThat(savedMember.getShortBio()).isEqualTo(shortBio);
        assertThat(savedMember.getPicture()).isEqualTo(picture);
        assertThat(savedMember.getRoleKey()).isEqualTo("ROLE_MEMBER");
        assertThat(savedMember.getEmailAuth()).isTrue();
    }

    @Test
    @DisplayName("username 존재 확인")
    @Transactional
    void exists_username() {
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

        //when
        Member member = requestDto.toEntity();
        Member savedMember = memberRepository.save(member);
        boolean existsByUsername = memberRepository.existsByUsername(savedMember.getUsername());

        //then
        assertThat(existsByUsername).isTrue();

    }

    @Test
    @DisplayName("username 존재시 에러 발생")
    @Transactional
    void already_exists_username() {
        //given
        String name = "슬로우스타터";
        String email = "azurealstn@naver.com";
        String username = "haha";
        Member member1 = Member.builder()
                .name(name)
                .email(email)
                .username(username)
                .role(Role.MEMBER)
                .emailAuth(true)
                .build();

        String username1 = "haha";
        Member member2 = Member.builder()
                .name(name)
                .email(email)
                .username(username1)
                .role(Role.MEMBER)
                .emailAuth(true)
                .build();

        //when
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        boolean existsByUsername = memberRepository.existsByUsername(savedMember2.getUsername());

        //then
        assertThrows(AlreadyExistsUsername.class, () -> memberService.validateExistsUsername(existsByUsername));
    }

    @Test
    @DisplayName("이메일 인증이 유효한지 체크")
    @Transactional
    void confirm_email_auth() {
        //given
        String email = "azurealstn@naver.com";
        LoginRequestDto requestDto = LoginRequestDto.builder()
                .email(email)
                .build();

        //when
        EmailAuth emailAuth = loginService.createEmailAuth(requestDto);
        Optional<EmailAuth> validAuthByEmail = emailAuthRepository.findValidAuthByEmail(email, emailAuth.getAuthToken(), LocalDateTime.now().plusHours(1L));

        //then
        assertThat(validAuthByEmail).isEmpty();
    }

    @Test
    @DisplayName("회원 단 건 조회 성공")
    @Transactional
    void findById_member_o() {
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

        Member member = requestDto.toEntity();
        Long savedId = memberRepository.save(member).getId();

        //when
        MemberResponseDto responseDto = memberService.findById(savedId);

        //then
        assertThat(responseDto.getName()).isEqualTo(name);
        assertThat(responseDto.getEmail()).isEqualTo(email);
        assertThat(responseDto.getShortBio()).isEqualTo(shortBio);
        assertThat(responseDto.getPicture()).isEqualTo(picture);
        assertThat(responseDto.getRole()).isEqualTo(Role.MEMBER);
    }

    @Test
    @DisplayName("회원 단 건 조회 실패")
    @Transactional
    void findById_member_x() {
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

        Member member = requestDto.toEntity();
        Long savedId = memberRepository.save(member).getId();

        //expected
        assertThrows(MemberNotFound.class, () -> memberService.findById(savedId + 1));
    }
}