package com.azurealstn.alog.dto.member;

import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.member.Role;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private Long id;
    private String email;
    private String name;
    private String username;
    private String shortBio;
    private String picture;
    private Role role;
    private Boolean emailAuth;
    private Boolean existsEmail;

    public MemberResponseDto(Member entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.name = entity.getName();
        this.username = entity.getUsername();
        this.shortBio = entity.getShortBio();
        this.picture = entity.getPicture();
        this.role = entity.getRole();
        this.emailAuth = entity.getEmailAuth();
    }

    public MemberResponseDto(boolean existsEmail) {
        this.existsEmail = existsEmail;
    }
}
