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
import com.azurealstn.alog.repository.posts.PostsRepository;
import com.azurealstn.alog.repository.tempsave.TempSaveRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Autowired
    private PostsRepository postsRepository;

    @BeforeEach
    public void beforeEach() throws Exception {
        tempSaveRepository.deleteAll();
        memberRepository.deleteAll();
        postsRepository.deleteAll();
        httpSession.invalidate();
    }

    @AfterEach
    public void afterEach() throws Exception {
        tempSaveRepository.deleteAll();
        memberRepository.deleteAll();
        postsRepository.deleteAll();
        httpSession.invalidate();
    }

    @Test
    @DisplayName("???????????? ??????")
    @Transactional
    public void create_tempSave() {
        //given
        String name = "??????????????????";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "???????????????!";
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
                .title("???????????????")
                .content("???????????????.")
                .member(savedMember)
                .build();

        //when
        tempSaveService.create(requestDto);
        List<TempSave> all = tempSaveRepository.findAll();
        TempSave tempSave = all.get(0);

        //then
        assertThat(tempSaveRepository.count()).isEqualTo(1L);
        assertThat(all.size()).isEqualTo(1);
        assertThat(tempSave.getTitle()).isEqualTo("???????????????");
        assertThat(tempSave.getContent()).isEqualTo("???????????????.");
        assertThat(tempSave.getMember().getEmail()).isEqualTo("azurealstn@naver.com");
        assertThat(tempSave.getMember().getName()).isEqualTo("??????????????????");
    }

    @Test
    @DisplayName("???????????? ??? ??? ?????? ??????")
    @Transactional(readOnly = true)
    void findById_tempSave_o() {
        //given
        String name = "??????????????????";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "???????????????!";
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
                .title("???????????????")
                .content("???????????????.")
                .member(savedMember)
                .build();

        Long savedId = tempSaveService.create(requestDto);

        //when
        TempSaveResponseDto responseDto = tempSaveService.findById(savedId);

        //then
        assertThat(tempSaveRepository.count()).isEqualTo(1L);
        assertThat(responseDto.getTitle()).isEqualTo("???????????????");
        assertThat(responseDto.getContent()).isEqualTo("???????????????.");
        assertThat(responseDto.getMember().getEmail()).isEqualTo("azurealstn@naver.com");
        assertThat(responseDto.getMember().getName()).isEqualTo("??????????????????");
    }

    @Test
    @DisplayName("???????????? ??? ??? ?????? ??????")
    @Transactional(readOnly = true)
    void findById_tempSave_x() {
        //given
        Long tempSaveId = 1L;

        //expected
        assertThrows(TempSaveNotFound.class, () -> tempSaveService.findById(tempSaveId));
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    @Transactional
    void update_tempSave_o() {
        //given
        String name = "??????????????????";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "???????????????!";
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
                .title("???????????????")
                .content("???????????????.")
                .member(savedMember)
                .build();

        tempSaveRepository.save(tempSave);


        //when
        TempSaveUpdateRequestDto requestDto = TempSaveUpdateRequestDto.builder()
                .title("?????? ???????????????.")
                .content("?????? ???????????????.")
                .build();
        Long updateId = tempSaveService.update(tempSave.getTempCode(), requestDto);
        TempSaveResponseDto responseDto = tempSaveService.findById(updateId);

        //then
        assertThat(responseDto.getTitle()).isEqualTo("?????? ???????????????.");
        assertThat(responseDto.getContent()).isEqualTo("?????? ???????????????.");
        assertThat(responseDto.getMember().getEmail()).isEqualTo("azurealstn@naver.com");
        assertThat(responseDto.getMember().getName()).isEqualTo("??????????????????");
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    @Transactional
    void update_tempSave_x() {
        //given
        String name = "??????????????????";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "???????????????!";
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
                .title("???????????????")
                .content("???????????????.")
                .member(savedMember)
                .build();

        tempSaveRepository.save(tempSave);


        //when
        TempSaveUpdateRequestDto requestDto = TempSaveUpdateRequestDto.builder()
                .title("?????? ???????????????.")
                .content("?????? ???????????????.")
                .build();

        //then
        assertThrows(TempSaveNotFound.class, () -> tempSaveService.update(tempSave.getTempCode() + 123, requestDto));
    }

    @Test
    @DisplayName("???????????? ??? ?????? ??????")
    @Transactional
    void temp_save_list() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());
        httpSession.setAttribute("member", new SessionMemberDto(savedMember));

        List<TempSave> collect = IntStream.range(0, 10)
                .mapToObj(i -> TempSave.builder()
                        .title("?????? ??????" + (i + 1))
                        .content("?????? ??????" + (i + 1))
                        .member(savedMember)
                        .build())
                .collect(Collectors.toList());
        tempSaveRepository.saveAll(collect);

        //when
        List<TempSave> tempSaveList = tempSaveRepository.findAll();

        //then
        assertThat(tempSaveList.size()).isEqualTo(10);
        assertThat(tempSaveList.get(0).getTitle()).isEqualTo("?????? ??????1");
        assertThat(tempSaveList.get(0).getContent()).isEqualTo("?????? ??????1");
        assertThat(tempSaveList.get(0).getMember().getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(tempSaveList.get(0).getMember().getName()).isEqualTo(savedMember.getName());
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    @Transactional
    void temp_save_delete_o() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());
        httpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("???????????????")
                .content("???????????????.")
                .member(savedMember)
                .tempCode(UUID.randomUUID().toString())
                .build();

        Long tempSaveId = tempSaveRepository.save(tempSave).getId();

        //when
        tempSaveService.delete(tempSaveId);

        //then
        assertThat(tempSaveRepository.count()).isEqualTo(0);

    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    @Transactional
    void temp_save_delete_x() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());
        httpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("???????????????")
                .content("???????????????.")
                .member(savedMember)
                .tempCode(UUID.randomUUID().toString())
                .build();

        Long tempSaveId = tempSaveRepository.save(tempSave).getId();

        //expected
        assertThrows(TempSaveNotFound.class, () -> tempSaveService.delete(tempSaveId + 1));

    }

    private MemberCreateRequestDto getMemberCreateRequestDto() {
        String name = "??????????????????";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "???????????????!";
        String picture = "test.jpg";

        return MemberCreateRequestDto.builder()
                .name(name)
                .email(email)
                .username(username)
                .shortBio(shortBio)
                .picture(picture)
                .build();

    }
}