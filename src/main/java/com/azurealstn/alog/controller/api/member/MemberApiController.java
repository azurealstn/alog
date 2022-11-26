package com.azurealstn.alog.controller.api.member;

import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.dto.member.MemberModifyRequestDto;
import com.azurealstn.alog.dto.member.MemberResponseDto;
import com.azurealstn.alog.dto.member.MemberSelectRequestDto;
import com.azurealstn.alog.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/auth/create-member")
    public Long createMember(@Valid @RequestBody MemberCreateRequestDto requestDto) {
        return memberService.create(requestDto);
    }

    @GetMapping("/api/v1/member/{memberId}")
    public MemberResponseDto findById(@PathVariable Long memberId) {
        return memberService.findById(memberId);
    }

    @PatchMapping("/api/v1/member-name/{memberId}")
    public MemberResponseDto modify_name(@PathVariable Long memberId, @RequestBody MemberModifyRequestDto requestDto) {
        return memberService.modify_name(memberId, requestDto);
    }

    @PatchMapping("/api/v1/member-username/{memberId}")
    public MemberResponseDto modify_username(@PathVariable Long memberId, @RequestBody MemberModifyRequestDto requestDto) {
        return memberService.modify_username(memberId, requestDto);
    }

    @PatchMapping("/api/v1/member-picture/{memberId}")
    public MemberResponseDto modify_picture(@PathVariable Long memberId, @RequestBody MemberModifyRequestDto requestDto) {
        return memberService.modify_picture(memberId, requestDto);
    }

    @DeleteMapping("/api/v1/member/{memberId}")
    public Long delete(@PathVariable Long memberId) {
        return memberService.delete(memberId);
    }
}
