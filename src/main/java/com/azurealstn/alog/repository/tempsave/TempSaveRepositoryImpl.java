package com.azurealstn.alog.repository.tempsave;

import com.azurealstn.alog.domain.tempsave.QTempSave;
import com.azurealstn.alog.domain.tempsave.TempSave;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.azurealstn.alog.domain.tempsave.QTempSave.*;

@RequiredArgsConstructor
public class TempSaveRepositoryImpl implements TempSaveRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<TempSave> findAll(Long memberId) {
        return jpaQueryFactory
                .selectFrom(tempSave)
                .where(tempSave.member.id.eq(memberId))
                .fetch();
    }
}
