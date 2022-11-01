package com.azurealstn.alog.service.login;

import com.azurealstn.alog.Infra.exception.MemberNotFound;
import com.azurealstn.alog.domain.email.EmailAuth;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.email.EmailAuthRequestDto;
import com.azurealstn.alog.dto.login.LoginRequestDto;
import com.azurealstn.alog.dto.login.LoginResponseDto;
import com.azurealstn.alog.dto.member.MemberSelectRequestDto;
import com.azurealstn.alog.repository.email.EmailAuthRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final EmailService emailService;
    private final HttpSession httpSession;

    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {
        boolean existsByEmail = memberRepository.existsByEmail(requestDto.getEmail());
        EmailAuth emailAuth = createEmailAuth(requestDto);
        EmailAuth savedEmailAuth = emailAuthRepository.save(emailAuth);
        if (existsByEmail) { //이메일 있음 O -> 로그인
            emailService.send(savedEmailAuth.getEmail(), savedEmailAuth.getAuthToken(), true);
            return new LoginResponseDto(savedEmailAuth.getEmail(), true);
        } else { //이메일 없음 X -> 회원가입 페이지로 이동
            emailService.send(savedEmailAuth.getEmail(), savedEmailAuth.getAuthToken(), false);
            return new LoginResponseDto(savedEmailAuth.getEmail(), false);
        }

    }

    @Transactional
    public void emailLogin(EmailAuthRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new MemberNotFound());
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(member.getRoleKey()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getEmail(), null, roles);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        httpSession.setAttribute("member", new SessionMemberDto(member));
    }

    @Transactional
    public void snsLogin(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFound());
        httpSession.setAttribute("member", new SessionMemberDto(member));
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public EmailAuth createEmailAuth(LoginRequestDto requestDto) {
        return EmailAuth.builder()
                .email(requestDto.getEmail())
                .authToken(UUID.randomUUID().toString())
                .expired(false)
                .build();
    }
}
