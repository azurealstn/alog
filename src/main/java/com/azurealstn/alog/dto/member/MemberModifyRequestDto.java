package com.azurealstn.alog.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class MemberModifyRequestDto {

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 45, message = "이름은 최대 45자까지 입력 할 수 있습니다.")
    private String name;

    @Pattern(regexp = "^[A-Za-z0-9_-]{3,16}$", message = "아이디는 3~16자의 알파벳,숫자,혹은 - _ 으로 이루어져야 합니다.")
    private String username;

    private String shortBio;

    private String picture;

    @Builder
    public MemberModifyRequestDto(String name, String username, String shortBio, String picture) {
        this.name = name;
        this.username = username;
        this.shortBio = shortBio;
        this.picture = picture;
    }
}
