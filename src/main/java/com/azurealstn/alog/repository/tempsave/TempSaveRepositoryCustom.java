package com.azurealstn.alog.repository.tempsave;

import com.azurealstn.alog.domain.tempsave.TempSave;

import java.util.List;

public interface TempSaveRepositoryCustom {

    List<TempSave> findAll(Long memberId);

    void deleteByTempCode(String tempCode);
}
