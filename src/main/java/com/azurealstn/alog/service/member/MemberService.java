package com.azurealstn.alog.service.member;

import com.azurealstn.alog.Infra.exception.member.AlreadyExistsName;
import com.azurealstn.alog.Infra.exception.member.AlreadyExistsUsername;
import com.azurealstn.alog.Infra.exception.emailauth.EmailAuthTokenNotFountException;
import com.azurealstn.alog.Infra.exception.member.MemberNotFound;
import com.azurealstn.alog.domain.email.EmailAuth;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.email.EmailAuthRequestDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.dto.member.MemberModifyRequestDto;
import com.azurealstn.alog.dto.member.MemberResponseDto;
import com.azurealstn.alog.repository.email.EmailAuthRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
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
     *
     * @return Long id
     */
    @Transactional
    public Long create(MemberCreateRequestDto requestDto) {
        boolean existsByUsername = memberRepository.existsByUsername(requestDto.getUsername());
        //boolean existsByName = memberRepository.existsByName(requestDto.getName());

        validateExistsUsername(existsByUsername);
        //validateExistsName(existsByName);

        Member member = requestDto.toEntity();
        return memberRepository.save(member).getId();
    }

    public void validateExistsUsername(boolean existsByUsername) {
        if (existsByUsername) {
            throw new AlreadyExistsUsername();
        }
    }

    public void validateExistsName(boolean existsByName) {
        if (existsByName) {
            throw new AlreadyExistsName();
        }
    }

    /**
     * 회원 단 건 조회 API
     */
    @Transactional
    public MemberResponseDto findById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFound());
        return new MemberResponseDto(member);
    }

    /**
     * 회원 수정 (name) API
     */
    @Transactional
    public MemberResponseDto modify_name(Long memberId, MemberModifyRequestDto requestDto) {
        boolean existsByName = memberRepository.existsByName(requestDto.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFound());

        validateExistsNameNotMe(requestDto, existsByName, member);

        Member modifiedMember = member.modify_name(requestDto.getName(), requestDto.getShortBio());

        return new MemberResponseDto(modifiedMember);
    }

    /**
     * 회원 수정 (username) API
     */
    @Transactional
    public MemberResponseDto modify_username(Long memberId, MemberModifyRequestDto requestDto) {
        boolean existsByUsername = memberRepository.existsByUsername(requestDto.getUsername());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFound());

        validateExistsUsernameNotMe(requestDto, existsByUsername, member);

        Member modifiedMember = member.modify_username(requestDto.getUsername());

        return new MemberResponseDto(modifiedMember);
    }

    /**
     * 회원 수정 (picture) API
     */
    @Transactional
    public MemberResponseDto modify_picture(Long memberId, MemberModifyRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFound());

        Member modifiedMember = member.modify_picture(requestDto.getPicture());

        return new MemberResponseDto(modifiedMember);
    }

    public void validateExistsUsernameNotMe(MemberModifyRequestDto requestDto, boolean existsByUsername, Member member) {
        if (!member.getUsername().equals(requestDto.getUsername()) && existsByUsername) {
            throw new AlreadyExistsUsername();
        }
    }

    public void validateExistsNameNotMe(MemberModifyRequestDto requestDto, boolean existsByName, Member member) {
        if (!member.getName().equals(requestDto.getName()) && existsByName) {
            throw new AlreadyExistsName();
        }
    }

    /**
     * 회원 영구 삭제 API
     */
    @Transactional
    public Long delete(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFound());
        memberRepository.delete(member);

        httpSession.invalidate();

        return memberId;
    }

    @Transactional(readOnly = true)
    public Boolean isMyPosts(Long memberId, SessionMemberDto sessionMemberDto) {
        return Objects.equals(memberId, sessionMemberDto.getId());
    }
}
