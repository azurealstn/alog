package com.azurealstn.alog.dto.tempsave;

import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.tempsave.TempSave;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class TempSaveCreateRequestDto {

    @NotBlank(message = "제목이 비어있습니다.")
    private String title;

    @NotBlank(message = "내용이 비어있습니다.")
    private String content;

    private Member member;

    private String tempCode;

    @Builder
    public TempSaveCreateRequestDto(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.tempCode = UUID.randomUUID().toString();
    }

    public TempSave toEntity() {
        return TempSave.builder()
                .title(title)
                .content(content)
                .member(member)
                .tempCode(UUID.randomUUID().toString())
                .build();
    }

    public void updateMember(Member member) {
        this.member = member;
    }
}
