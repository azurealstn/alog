package com.azurealstn.alog.repository.tempsave;

import com.azurealstn.alog.domain.tempsave.TempSave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempSaveRepository extends JpaRepository<TempSave, Long> {

    Optional<TempSave> findByTempCode(String tempCode);
}
