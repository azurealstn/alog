package com.azurealstn.alog.dto.login;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {

    private String email;
    private boolean existsEmail;

    public LoginResponseDto(String email, boolean existsEmail) {
        this.email = email;
        this.existsEmail = existsEmail;
    }
}
