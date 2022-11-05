package com.azurealstn.alog.service.member;

import com.azurealstn.alog.Infra.exception.member.AlreadyExistsUsername;
import com.azurealstn.alog.Infra.exception.emailauth.EmailAuthTokenNotFountException;
import com.azurealstn.alog.Infra.exception.member.MemberNotFound;
import com.azurealstn.alog.domain.email.EmailAuth;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.email.EmailAuthRequestDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.dto.member.MemberResponseDto;
import com.azurealstn.alog.repository.email.EmailAuthRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final HttpSession httpSession;

    /**
     * 인증이 가능한 토큰값을 가져온다. -> 없으면 Exception 발생
     * 토큰이 있으면 다시 토큰을 만료시킨다.
     */
    @Transactional
    public MemberResponseDto confirmEmailAuth(EmailAuthRequestDto requestDto) {
        EmailAuth emailAuth = emailAuthRepository.findValidAuthByEmail(requestDto.getEmail(), requestDto.getAuthToken(), LocalDateTime.now())
                .orElseThrow(() -> new EmailAuthTokenNotFountException());

        emailAuth.useToken();

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .build();

        return new MemberResponseDto(member);
    }

    /**
     * 회원가입
     * @return Long id
     */
    @Transactional
    public Long create(MemberCreateRequestDto requestDto) {
        boolean existsByUsername = memberRepository.existsByUsername(requestDto.getUsername());

        validateExistsUsername(existsByUsername);

        Member member = requestDto.toEntity();
        return memberRepository.save(member).getId();
    }

    public void validateExistsUsername(boolean existsByUsername) {
        if (existsByUsername) {
            throw new AlreadyExistsUsername();
        }
    }

    @Transactional
    public MemberResponseDto findById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFound());
        return new MemberResponseDto(member);
    }

}
