package com.azurealstn.alog.controller.api.login;

import com.azurealstn.alog.dto.login.LoginRequestDto;
import com.azurealstn.alog.dto.login.LoginResponseDto;
import com.azurealstn.alog.service.login.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginApiController {

    private final LoginService loginService;

    @PostMapping("/api/v1/auth/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto requestDto) {
        return loginService.login(requestDto);
    }

}
