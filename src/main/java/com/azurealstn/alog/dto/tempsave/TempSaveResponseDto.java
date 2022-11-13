package com.azurealstn.alog.dto.tempsave;

import com.azurealstn.alog.Infra.utils.DateUtils;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.tempsave.TempSave;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
public class TempSaveResponseDto {

    private final Long id;
    private final String title;
    private final String content;

    @JsonIgnore
    private final Member member;

    private final String tempCode;
    private final String previousTime;

    public TempSaveResponseDto(TempSave entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.member = entity.getMember();
        this.tempCode = entity.getTempCode();
        this.previousTime = DateUtils.previousTimeCalc(entity.getCreatedDate());
    }
}
