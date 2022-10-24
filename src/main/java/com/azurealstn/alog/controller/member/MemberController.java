package com.azurealstn.alog.controller.member;

import com.azurealstn.alog.config.auth.CustomOAuth2UserService;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.dto.email.EmailAuthRequestDto;
import com.azurealstn.alog.dto.member.MemberResponseDto;
import com.azurealstn.alog.service.login.LoginService;
import com.azurealstn.alog.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final LoginService loginService;

    @GetMapping("/api/v1/auth/create-member")
    public String createMember(@ModelAttribute EmailAuthRequestDto requestDto, Model model) {
        MemberResponseDto memberResponseDto = memberService.confirmEmailAuth(requestDto);

        model.addAttribute("member", memberResponseDto);

        return "member/create-member";
    }

    @GetMapping("/api/v1/auth/sns-create-member-login")
    public String snsCreateMemberLogin(Model model) {
        Member member = customOAuth2UserService.getSnsMemberInfo();
        boolean existsByEmail = loginService.existsByEmail(member.getEmail());

        log.info("picture={}", member.getPicture());

        model.addAttribute("member", member);

        if (existsByEmail) {
            return "redirect:/api/v1/auth/snsLogin?email=" + member.getEmail();
        } else {
            return "member/create-member";
        }
    }



}
