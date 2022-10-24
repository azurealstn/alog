package com.azurealstn.alog.dto.auth;

import com.azurealstn.alog.domain.member.Member;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 소셜로그인을 통해 인증된 사용자 정보만을 담고 있다.
 */
@ToString
@Getter
public class SessionMemberDto implements Serializable {

    private final String name;
    private final String email;
    private final String picture;
    private final String username;
    private final String shortBio;

    public SessionMemberDto(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.picture = member.getPicture();
        this.username = member.getUsername();
        this.shortBio = member.getShortBio();
    }
}
