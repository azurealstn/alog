package com.azurealstn.alog.controller.api.member;

import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
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
}
