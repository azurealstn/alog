package com.azurealstn.alog.repository.email;

import com.azurealstn.alog.domain.email.EmailAuth;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.azurealstn.alog.domain.email.QEmailAuth.emailAuth;

@RequiredArgsConstructor
public class EmailAuthRepositoryImpl implements EmailAuthRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 클라이언트로부터 받은 파라미터 값과
     * DB에 저장되어 있는 email, authToken 값이 일치하는지 비교
     * 만료 시간이 지나지 않고, 만료되지 않은 토큰일 경우에 인증 성공
     */
    @Override
    public Optional<EmailAuth> findValidAuthByEmail(String email, String authToken, LocalDateTime currentTime) {
        EmailAuth entity = jpaQueryFactory
                .selectFrom(emailAuth)
                .where(emailAuth.email.eq(email),
                        emailAuth.authToken.eq(authToken),
                        emailAuth.expiredDate.goe(currentTime),
                        emailAuth.expired.eq(false))
                .fetchFirst();
        return Optional.ofNullable(entity);
    }
}
