package com.azurealstn.alog.service.tempsave;

import com.azurealstn.alog.Infra.exception.member.MemberNotFound;
import com.azurealstn.alog.Infra.exception.tempsave.TempSaveNotFound;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.tempsave.TempSave;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.tempsave.TempSaveCreateRequestDto;
import com.azurealstn.alog.dto.tempsave.TempSaveResponseDto;
import com.azurealstn.alog.dto.tempsave.TempSaveUpdateRequestDto;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.repository.tempsave.TempSaveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TempSaveService {

    private final TempSaveRepository tempSaveRepository;
    private final HttpSession httpSession;
    private final MemberRepository memberRepository;

    /**
     * 임시저장 등록 API
     */
    @Transactional
    public Long create(TempSaveCreateRequestDto requestDto) {
        SessionMemberDto sessionMemberDto = (SessionMemberDto) httpSession.getAttribute("member");
        Member member = memberRepository.findByEmail(sessionMemberDto.getEmail())
                .orElseThrow(() -> new MemberNotFound());
        requestDto.updateMember(member);
        TempSave tempSave = requestDto.toEntity();
        return tempSaveRepository.save(tempSave).getId();
    }

    /**
     * 임시저장 단건 조회 API
     */
    @Transactional(readOnly = true)
    public TempSaveResponseDto findById(Long tempSaveId) {
        TempSave tempSave = tempSaveRepository.findById(tempSaveId)
                .orElseThrow(() -> new TempSaveNotFound());
        return new TempSaveResponseDto(tempSave);
    }

    /**
     * 임시저장 단건 조회 (TempCode) API
     */
    @Transactional(readOnly = true)
    public TempSaveResponseDto findByTempCode(String tempCode) {
        TempSave tempSave = tempSaveRepository.findByTempCode(tempCode)
                .orElseThrow(() -> new TempSaveNotFound());
        return new TempSaveResponseDto(tempSave);
    }

    /**
     * 임시저장 수정 API
     */
    @Transactional
    public Long update(String tempCode, TempSaveUpdateRequestDto requestDto) {
        TempSave tempSave = tempSaveRepository.findByTempCode(tempCode)
                .orElseThrow(() -> new TempSaveNotFound());
        tempSave.update(requestDto.getTitle(), requestDto.getContent());
        return tempSave.getId();
    }

    /**
     * 임시저장 전체 조회 API
     */
    @Transactional(readOnly = true)
    public List<TempSaveResponseDto> findAll(Long memberId) {
        List<TempSave> tempSaveList = tempSaveRepository.findAll(memberId);
        return tempSaveList.stream()
                .map(tempSave -> new TempSaveResponseDto(tempSave))
                .collect(Collectors.toList());
    }

    /**
     * 임시저장 삭제 API
     */
    @Transactional
    public Long delete(Long tempSaveId) {
        TempSave tempSave = tempSaveRepository.findById(tempSaveId)
                .orElseThrow(() -> new TempSaveNotFound());
        tempSaveRepository.delete(tempSave);
        return tempSaveId;
    }

    /**
     * 임시저장 삭제 by tempCode API
     */
    @Transactional
    public String deleteByTempCode(String tempCode) {
        tempSaveRepository.deleteByTempCode(tempCode);
        return tempCode;
    }
}
