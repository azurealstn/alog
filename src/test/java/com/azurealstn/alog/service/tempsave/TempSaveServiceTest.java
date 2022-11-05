package com.azurealstn.alog.service.tempsave;

import com.azurealstn.alog.Infra.exception.tempsave.TempSaveNotFound;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.tempsave.TempSave;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.dto.tempsave.TempSaveCreateRequestDto;
import com.azurealstn.alog.dto.tempsave.TempSaveResponseDto;
import com.azurealstn.alog.dto.tempsave.TempSaveUpdateRequestDto;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.repository.tempsave.TempSaveRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@WithMockUser(roles = "MEMBER")
@SpringBootTest
class TempSaveServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private TempSaveRepository tempSaveRepository;

    @Autowired
    private TempSaveService tempSaveService;

    @BeforeEach
    public void beforeEach() throws Exception {
        tempSaveRepository.deleteAll();
        memberRepository.deleteAll();
        httpSession.invalidate();
    }

    @Test
    @DisplayName("임시저장 생성")
    @Transactional
    public void create_tempSave() {
        //given
        String name = "슬로우스타터";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "안녕하세요!";
        String picture = "test.jpg";

        MemberCreateRequestDto memberCreateRequestDto = MemberCreateRequestDto.builder()
                .name(name)
                .email(email)
                .username(username)
                .shortBio(shortBio)
                .picture(picture)
                .build();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());
        httpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSaveCreateRequestDto requestDto = TempSaveCreateRequestDto.builder()
                .title("제목입니다")
                .content("내용입니다.")
                .member(savedMember)
                .build();

        //when
        tempSaveService.create(requestDto);
        List<TempSave> all = tempSaveRepository.findAll();
        TempSave tempSave = all.get(0);

        //then
        assertThat(tempSaveRepository.count()).isEqualTo(1L);
        assertThat(all.size()).isEqualTo(1);
        assertThat(tempSave.getTitle()).isEqualTo("제목입니다");
        assertThat(tempSave.getContent()).isEqualTo("내용입니다.");
        assertThat(tempSave.getMember().getEmail()).isEqualTo("azurealstn@naver.com");
        assertThat(tempSave.getMember().getName()).isEqualTo("슬로우스타터");
    }

    @Test
    @DisplayName("임시저장 단 건 조회 성공")
    @Transactional(readOnly = true)
    void findById_tempSave_o() {
        //given
        String name = "슬로우스타터";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "안녕하세요!";
        String picture = "test.jpg";

        MemberCreateRequestDto memberCreateRequestDto = MemberCreateRequestDto.builder()
                .name(name)
                .email(email)
                .username(username)
                .shortBio(shortBio)
                .picture(picture)
                .build();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());
        httpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSaveCreateRequestDto requestDto = TempSaveCreateRequestDto.builder()
                .title("제목입니다")
                .content("내용입니다.")
                .member(savedMember)
                .build();

        Long savedId = tempSaveService.create(requestDto);

        //when
        TempSaveResponseDto responseDto = tempSaveService.findById(savedId);

        //then
        assertThat(tempSaveRepository.count()).isEqualTo(1L);
        assertThat(responseDto.getTitle()).isEqualTo("제목입니다");
        assertThat(responseDto.getContent()).isEqualTo("내용입니다.");
        assertThat(responseDto.getMember().getEmail()).isEqualTo("azurealstn@naver.com");
        assertThat(responseDto.getMember().getName()).isEqualTo("슬로우스타터");
    }

    @Test
    @DisplayName("임시저장 단 건 조회 실패")
    @Transactional(readOnly = true)
    void findById_tempSave_x() {
        //given
        Long tempSaveId = 1L;

        //expected
        assertThrows(TempSaveNotFound.class, () -> tempSaveService.findById(tempSaveId));
    }

    @Test
    @DisplayName("임시저장 수정 성공")
    @Transactional
    void update_tempSave_o() {
        //given
        String name = "슬로우스타터";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "안녕하세요!";
        String picture = "test.jpg";

        MemberCreateRequestDto memberCreateRequestDto = MemberCreateRequestDto.builder()
                .name(name)
                .email(email)
                .username(username)
                .shortBio(shortBio)
                .picture(picture)
                .build();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());
        httpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("제목입니다")
                .content("내용입니다.")
                .member(savedMember)
                .build();

        tempSaveRepository.save(tempSave);


        //when
        TempSaveUpdateRequestDto requestDto = TempSaveUpdateRequestDto.builder()
                .title("수정 제목입니다.")
                .content("수정 내용입니다.")
                .build();
        Long updateId = tempSaveService.update(tempSave.getTempCode(), requestDto);
        TempSaveResponseDto responseDto = tempSaveService.findById(updateId);

        //then
        assertThat(responseDto.getTitle()).isEqualTo("수정 제목입니다.");
        assertThat(responseDto.getContent()).isEqualTo("수정 내용입니다.");
        assertThat(responseDto.getMember().getEmail()).isEqualTo("azurealstn@naver.com");
        assertThat(responseDto.getMember().getName()).isEqualTo("슬로우스타터");
    }

    @Test
    @DisplayName("임시저장 수정 실패")
    @Transactional
    void update_tempSave_x() {
        //given
        String name = "슬로우스타터";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "안녕하세요!";
        String picture = "test.jpg";

        MemberCreateRequestDto memberCreateRequestDto = MemberCreateRequestDto.builder()
                .name(name)
                .email(email)
                .username(username)
                .shortBio(shortBio)
                .picture(picture)
                .build();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());
        httpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("제목입니다")
                .content("내용입니다.")
                .member(savedMember)
                .build();

        tempSaveRepository.save(tempSave);


        //when
        TempSaveUpdateRequestDto requestDto = TempSaveUpdateRequestDto.builder()
                .title("수정 제목입니다.")
                .content("수정 내용입니다.")
                .build();

        //then
        assertThrows(TempSaveNotFound.class, () -> tempSaveService.update(tempSave.getTempCode() + 123, requestDto));
    }
}