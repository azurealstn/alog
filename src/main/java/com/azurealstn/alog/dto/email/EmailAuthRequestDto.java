package com.azurealstn.alog.dto.email;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class EmailAuthRequestDto {

    private final String email;
    private final String authToken;
    private final Boolean expired;
    private final LocalDateTime expiredDate;

    @Builder
    public EmailAuthRequestDto(String email, String authToken, Boolean expired, LocalDateTime expiredDate) {
        this.email = email;
        this.authToken = authToken;
        this.expired = expired;
        this.expiredDate = expiredDate;
    }
}
