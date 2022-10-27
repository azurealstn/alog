package com.azurealstn.alog.dto.email;

import com.azurealstn.alog.domain.email.EmailAuth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class EmailAuthRequestDto {

    private final String email;
    private final String authToken;
    private final Boolean expired;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime expiredDate;

    @Builder
    public EmailAuthRequestDto(String email, String authToken, Boolean expired, LocalDateTime expiredDate) {
        this.email = email;
        this.authToken = authToken;
        this.expired = expired;
        this.expiredDate = expiredDate;
    }

    public EmailAuth toEntity() {
        return EmailAuth.builder()
                .email(email)
                .authToken(authToken)
                .expired(expired)
                .build();
    }
}
