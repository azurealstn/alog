package com.azurealstn.alog.controller.api.tempsave;

import com.azurealstn.alog.dto.tempsave.TempSaveCreateRequestDto;
import com.azurealstn.alog.dto.tempsave.TempSaveResponseDto;
import com.azurealstn.alog.dto.tempsave.TempSaveUpdateRequestDto;
import com.azurealstn.alog.service.tempsave.TempSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TempSaveApiController {

    private final TempSaveService tempSaveService;

    @PostMapping("/api/v1/temp-save")
    public Long create(@Valid @RequestBody TempSaveCreateRequestDto requestDto) {
        return tempSaveService.create(requestDto);
    }

    @GetMapping("/api/v1/temp-save/{tempSaveId}")
    public TempSaveResponseDto findById(@PathVariable Long tempSaveId) {
        return tempSaveService.findById(tempSaveId);
    }

    @PutMapping("/api/v1/temp-save/{tempCode}")
    public Long update(@Valid @RequestBody TempSaveUpdateRequestDto requestDto, @PathVariable String tempCode) {
        return tempSaveService.update(tempCode, requestDto);
    }

}
