package com.azurealstn.alog.dto.image;

import com.azurealstn.alog.domain.image.MemberImage;
import com.azurealstn.alog.domain.member.Member;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
public class MemberImageResponseDto implements Serializable {

    private Long id;
    private Member member;
    private String originalFilename; //사용자가 업로드한 파일 이름
    private String storeFilename; //서버 내부에서 관리하는 파일명
    private String imageUrl;
    private Boolean thumbnailYn;

    public MemberImageResponseDto(MemberImage entity) {
        this.id = entity.getId();
        this.member = entity.getMember();
        this.originalFilename = entity.getOriginalFilename();
        this.storeFilename = entity.getStoreFilename();
        this.imageUrl = entity.getImageUrl();
        this.thumbnailYn = entity.getThumbnailYn();
    }


}
