package com.azurealstn.alog.controller.api.login;

import com.azurealstn.alog.dto.login.LoginRequestDto;
import com.azurealstn.alog.dto.login.LoginResponseDto;
import com.azurealstn.alog.service.login.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginApiController {

    private final LoginService loginService;

    @PostMapping("/api/v1/auth/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto requestDto) throws MessagingException {
        return loginService.login(requestDto);
    }

    @GetMapping("/api/v1/auth/login-after-create/{memberId}")
    public void loginAfterCreate(@PathVariable Long memberId) {
        loginService.createAfterLogin(memberId);
    }

}
