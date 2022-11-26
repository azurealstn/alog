package com.azurealstn.alog.domain.image;

import com.azurealstn.alog.domain.BaseTimeEntity;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import lombok.*;

import javax.persistence.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MemberImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String originalFilename; //사용자가 업로드한 파일 이름

    @Column
    private String storeFilename; //서버 내부에서 관리하는 파일명

    @Column
    private String imageUrl;

    @Column
    private Boolean thumbnailYn;

    @Builder
    public MemberImage(Member member, String originalFilename, String storeFilename, String imageUrl, Boolean thumbnailYn) {
        this.member = member;
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.imageUrl = imageUrl;
        this.thumbnailYn = thumbnailYn;
    }
}
