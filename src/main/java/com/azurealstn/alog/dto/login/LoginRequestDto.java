package com.azurealstn.alog.dto.login;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

    @Email(message = "잘못된 이메일 형식입니다.")
    @NotBlank(message = "잘못된 이메일 형식입니다.")
    private String email;
    private String authToken;
    private boolean existsEmail;

    @Builder
    public LoginRequestDto(String email, String authToken, boolean existsEmail) {
        this.email = email;
        this.authToken = authToken;
        this.existsEmail = existsEmail;
    }
}

