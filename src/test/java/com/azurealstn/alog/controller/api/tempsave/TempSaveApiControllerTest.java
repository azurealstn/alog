package com.azurealstn.alog.controller.api.tempsave;

import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.tempsave.TempSave;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.dto.tempsave.TempSaveCreateRequestDto;
import com.azurealstn.alog.dto.tempsave.TempSaveUpdateRequestDto;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.repository.posts.PostsRepository;
import com.azurealstn.alog.repository.tempsave.TempSaveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(roles = "MEMBER")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TempSaveApiControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TempSaveRepository tempSaveRepository;

    @Autowired
    private PostsRepository postsRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        postsRepository.deleteAll();
        tempSaveRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        postsRepository.deleteAll();
        tempSaveRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("???????????? ????????? ????????? ?????? ??????")
    @Transactional
    void create_tempSave_valid_title() throws Exception {
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSaveCreateRequestDto requestDto = TempSaveCreateRequestDto.builder()
                .title("    ")
                .content("????????????????")
                .member(savedMember)
                .build();
        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "?????????????????? ????????? ????????? ????????????. (application/json)";

        //expected
        mockMvc.perform(post("/api/v1/temp-save")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation.length()", is(1)))
                .andExpect(jsonPath("$.validation").isNotEmpty())
                .andExpect(jsonPath("$.validation[0].fieldName").value("title"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("???????????? ????????? ????????? ?????? ??????")
    @Transactional
    void create_tempSave_valid_content() throws Exception {
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSaveCreateRequestDto requestDto = TempSaveCreateRequestDto.builder()
                .title("????????????????")
                .content("    ")
                .member(savedMember)
                .build();
        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "?????????????????? ????????? ????????? ????????????. (application/json)";

        //expected
        mockMvc.perform(post("/api/v1/temp-save")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation.length()", is(1)))
                .andExpect(jsonPath("$.validation").isNotEmpty())
                .andExpect(jsonPath("$.validation[0].fieldName").value("content"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    @Transactional
    void create_tempSave() throws Exception {
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSaveCreateRequestDto requestDto = TempSaveCreateRequestDto.builder()
                .title("????????????????")
                .content("????????????????")
                .member(savedMember)
                .build();
        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "?????????????????? ????????? ????????? ????????????. (application/json)";

        //expected
        mockMvc.perform(post("/api/v1/temp-save")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("???????????? ??? ??? ?????? ??????")
    @Transactional(readOnly = true)
    void findById_tempSave_x() throws Exception {
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("???????????????")
                .content("???????????????.")
                .member(savedMember)
                .build();

        Long savedId = tempSaveRepository.save(tempSave).getId();

        //expected
        mockMvc.perform(get("/api/v1/temp-save/{tempSaveId}", savedId + 1)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("???????????? ??? ??? ?????? ??????")
    @Transactional(readOnly = true)
    void findById_tempSave_o() throws Exception {
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("???????????????")
                .content("???????????????.")
                .member(savedMember)
                .build();

        Long savedId = tempSaveRepository.save(tempSave).getId();
        String expectedTitle = "???????????????";
        String expectedContent = "???????????????.";

        //expected
        mockMvc.perform(get("/api/v1/temp-save/{tempSaveId}", savedId)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedId))
                .andExpect(jsonPath("$.title", is(expectedTitle)))
                .andExpect(jsonPath("$.content", is(expectedContent)))
                .andDo(print());
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    @Transactional
    void update_tempSave_x() throws Exception {
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("???????????????")
                .content("???????????????.")
                .member(savedMember)
                .tempCode(UUID.randomUUID().toString())
                .build();

        tempSaveRepository.save(tempSave);

        TempSaveUpdateRequestDto requestDto = TempSaveUpdateRequestDto.builder()
                .title("?????? ???????????????.")
                .content("?????? ???????????????.")
                .build();

        //expected
        mockMvc.perform(put("/api/v1/temp-save/{tempCode}", tempSave.getTempCode() + 123)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ????????? ?????? ??????")
    @Transactional
    void update_tempSave_x_title() throws Exception {
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("???????????????.")
                .content("???????????????.")
                .member(savedMember)
                .tempCode(UUID.randomUUID().toString())
                .build();

        tempSaveRepository.save(tempSave);

        TempSaveUpdateRequestDto requestDto = TempSaveUpdateRequestDto.builder()
                .title("     ")
                .content("?????? ???????????????.")
                .build();

        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "?????????????????? ????????? ????????? ????????????. (application/json)";

        //expected
        mockMvc.perform(put("/api/v1/temp-save/{tempCode}", tempSave.getTempCode())
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation.length()", is(1)))
                .andExpect(jsonPath("$.validation").isNotEmpty())
                .andExpect(jsonPath("$.validation[0].fieldName").value("title"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ????????? ?????? ??????")
    @Transactional
    void update_tempSave_x_content() throws Exception {
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("???????????????")
                .content("???????????????.")
                .member(savedMember)
                .tempCode(UUID.randomUUID().toString())
                .build();

        tempSaveRepository.save(tempSave);

        TempSaveUpdateRequestDto requestDto = TempSaveUpdateRequestDto.builder()
                .title("?????? ???????????????.")
                .content("   ")
                .build();

        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "?????????????????? ????????? ????????? ????????????. (application/json)";

        //expected
        mockMvc.perform(put("/api/v1/temp-save/{tempCode}", tempSave.getTempCode())
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation.length()", is(1)))
                .andExpect(jsonPath("$.validation").isNotEmpty())
                .andExpect(jsonPath("$.validation[0].fieldName").value("content"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    @Transactional
    void update_tempSave_o() throws Exception {
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("???????????????")
                .content("???????????????.")
                .member(savedMember)
                .tempCode(UUID.randomUUID().toString())
                .build();

        tempSaveRepository.save(tempSave);

        TempSaveUpdateRequestDto requestDto = TempSaveUpdateRequestDto.builder()
                .title("?????? ???????????????.")
                .content("?????? ???????????????.")
                .build();

        //expected
        mockMvc.perform(put("/api/v1/temp-save/{tempCode}", tempSave.getTempCode())
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    @Transactional
    void temp_save_delete_o() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("???????????????")
                .content("???????????????.")
                .member(savedMember)
                .tempCode(UUID.randomUUID().toString())
                .build();

        Long tempSaveId = tempSaveRepository.save(tempSave).getId();

        //expected
        mockMvc.perform(delete("/api/v1/temp-save/{tempSaveId}", tempSaveId)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    @Transactional
    void temp_save_delete_x() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("???????????????")
                .content("???????????????.")
                .member(savedMember)
                .tempCode(UUID.randomUUID().toString())
                .build();

        Long tempSaveId = tempSaveRepository.save(tempSave).getId();

        //expected
        mockMvc.perform(delete("/api/v1/temp-save/{tempSaveId}", tempSaveId + 1)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

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