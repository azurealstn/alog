package com.azurealstn.alog.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSelectRequestDto {

    private Long id;
    private String email;
    private boolean existsEmail;

    @Builder
    public MemberSelectRequestDto(Long id, String email, boolean existsEmail) {
        this.id = id;
        this.email = email;
        this.existsEmail = existsEmail;
    }
}
