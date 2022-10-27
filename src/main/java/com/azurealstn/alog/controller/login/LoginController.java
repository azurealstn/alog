package com.azurealstn.alog.controller.login;

import com.azurealstn.alog.dto.email.EmailAuthRequestDto;
import com.azurealstn.alog.dto.login.LoginRequestDto;
import com.azurealstn.alog.service.login.LoginService;
import com.azurealstn.alog.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {

    private final LoginService loginService;
    private final MemberService memberService;

    @GetMapping("/api/v1/auth/email-login")
    public String emailLogin(@ModelAttribute EmailAuthRequestDto requestDto) {
        memberService.confirmEmailAuth(requestDto);
        loginService.emailLogin(requestDto);
        return "redirect:/";
    }

    @GetMapping("/api/v1/auth/snsLogin")
    public String snsLogin(@RequestParam String email) {
        loginService.snsLogin(email);
        return "redirect:/";
    }
}
