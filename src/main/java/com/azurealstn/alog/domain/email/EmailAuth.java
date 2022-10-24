package com.azurealstn.alog.domain.email;

import com.azurealstn.alog.domain.BaseTimeEntity;
import com.azurealstn.alog.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * email: 가입 이메일
 * authToken: 사용자 구별 UUID
 * expired: 만료 여부
 * expireDate: 만료 시간
 * 이메일 인증 유효 시간: 5분 설정
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class EmailAuth extends BaseTimeEntity {

    private static final Long MAX_EXPIRE_TIME = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_auth_id")
    private Long id;

    @Column
    private String email;

    @Column
    private String authToken;

    @Column
    private Boolean expired;

    @Column
    private LocalDateTime expiredDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public EmailAuth(String email, String authToken, Boolean expired) {
        this.email = email;
        this.authToken = authToken;
        this.expired = expired;
        this.expiredDate = LocalDateTime.now().plusMinutes(MAX_EXPIRE_TIME);
    }

    public void useToken() {
        this.expired = true;
    }
}
