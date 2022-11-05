package com.azurealstn.alog.domain.tempsave;

import com.azurealstn.alog.domain.BaseTimeEntity;
import com.azurealstn.alog.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TempSave extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temp_save_id")
    private Long id;

    @Column
    private String title;

    @Column
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String tempCode;

    @Builder
    public TempSave(String title, String content, Member member, String tempCode) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.tempCode = tempCode;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
