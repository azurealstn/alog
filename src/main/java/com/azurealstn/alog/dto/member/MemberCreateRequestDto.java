package com.azurealstn.alog.dto.member;

import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.member.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
public class MemberCreateRequestDto {

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 45, message = "이름은 최대 45자까지 입력 할 수 있습니다.")
    private String name;

    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @Pattern(regexp = "^[A-Za-z0-9_-]{3,16}$", message = "아이디는 3~16자의 알파벳,숫자,혹은 - _ 으로 이루어져야 합니다.")
    private String username;

    private String shortBio;
    private String picture;
    private Role role;
    private Boolean emailAuth;

    @Builder
    public MemberCreateRequestDto(String name, String email, String username, String shortBio, String picture, Role role, Boolean emailAuth) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.shortBio = shortBio;
        this.picture = picture;
        this.role = role;
        this.emailAuth = emailAuth;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .role(Role.MEMBER)
                .emailAuth(true)
                .username(username)
                .shortBio(shortBio)
                .build();
    }
}
