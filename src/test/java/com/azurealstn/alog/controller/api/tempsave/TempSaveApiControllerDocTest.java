package com.azurealstn.alog.controller.api.tempsave;

import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.domain.tempsave.TempSave;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsModifyRequestDto;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.alog.com", uriPort = 443)
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
public class TempSaveApiControllerDocTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RestDocumentationContextProvider restDocumentationContextProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private TempSaveRepository tempSaveRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentationContextProvider))
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
    @DisplayName("임시저장 단건 조회")
    @WithMockUser("MEMBER")
    @Transactional
    void findById_api() throws Exception {
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("제목입니다")
                .content("내용입니다.")
                .member(savedMember)
                .build();

        Long savedId = tempSaveRepository.save(tempSave).getId();

        mockMvc.perform(get("/api/v1/temp-save/{tempSaveId}", savedId)
                        .session(mockHttpSession)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("tempsave-findById", pathParameters(
                                parameterWithName("tempSaveId").description("임시저장 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("임시저장 ID"),
                                fieldWithPath("title").description("임시저장 제목"),
                                fieldWithPath("content").description("임시저장 내용"),
                                fieldWithPath("tempCode").description("임시저장 코드"),
                                fieldWithPath("previousTime").description("임시저장 전에 등록한 시간")
                        )
                ));
    }

    @Test
    @DisplayName("임시저장 작성")
    @WithMockUser("MEMBER")
    @Transactional
    void create_tempsave_api() throws Exception {
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSaveCreateRequestDto requestDto = TempSaveCreateRequestDto.builder()
                .title("제목이지롱?")
                .content("내용이지롱?")
                .member(savedMember)
                .build();
        String body = objectMapper.writeValueAsString(requestDto);

        //expected
        mockMvc.perform(post("/api/v1/temp-save")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("tempsave-create",
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("임시저장 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("임시저장 내용"),
                                fieldWithPath("tempCode").type(JsonFieldType.STRING).description("임시저장 코드"),
                                fieldWithPath("member").type(JsonFieldType.OBJECT).description("회원 정보"),
                                fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("member.email").type(JsonFieldType.STRING).description("회원 이메일"),
                                fieldWithPath("member.name").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("member.picture").type(JsonFieldType.STRING).description("회원 프로필사진"),
                                fieldWithPath("member.role").type(JsonFieldType.STRING).description("회원 권한"),
                                fieldWithPath("member.emailAuth").type(JsonFieldType.BOOLEAN).description("회원 인증 상태"),
                                fieldWithPath("member.username").type(JsonFieldType.STRING).description("회원 아이디"),
                                fieldWithPath("member.shortBio").type(JsonFieldType.STRING).description("회원 한 줄 소개"),
                                fieldWithPath("member.roleKey").type(JsonFieldType.STRING).description("회원 권한 키"),
                                fieldWithPath("member.createdDate").type(JsonFieldType.STRING).description("회원 생성날짜"),
                                fieldWithPath("member.modifiedDate").type(JsonFieldType.STRING).description("회원 수정날짜")

                        )
                ));

    }

    @Test
    @DisplayName("임시저장 수정")
    @WithMockUser("MEMBER")
    @Transactional
    void modify_tempsave_api() throws Exception {
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("제목입니다")
                .content("내용입니다.")
                .member(savedMember)
                .tempCode(UUID.randomUUID().toString())
                .build();

        tempSaveRepository.save(tempSave);

        TempSaveUpdateRequestDto requestDto = TempSaveUpdateRequestDto.builder()
                .title("수정 제목입니다.")
                .content("수정 내용입니다.")
                .build();

        //expected
        mockMvc.perform(put("/api/v1/temp-save/{tempCode}", tempSave.getTempCode())
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("tempsave-modify",
                        pathParameters(
                                parameterWithName("tempCode").description("임시저장 코드")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("임시저장 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("임시저장 내용")
                        )
                ));
    }

    @Test
    @DisplayName("임시저장 삭제")
    @WithMockUser("MEMBER")
    @Transactional
    void delete_tempsave_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        TempSave tempSave = TempSave.builder()
                .title("제목입니다")
                .content("내용입니다.")
                .member(savedMember)
                .tempCode(UUID.randomUUID().toString())
                .build();

        Long tempSaveId = tempSaveRepository.save(tempSave).getId();

        //expected
        mockMvc.perform(delete("/api/v1/temp-save/{tempSaveId}", tempSaveId)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("tempsave-delete",
                        pathParameters(
                                parameterWithName("tempSaveId").description("임시저장 ID")
                        )
                ));
    }

    private MemberCreateRequestDto getMemberCreateRequestDto() {
        String name = "슬로우스타터";
        String email = "azurealstn@naver.com";
        String username = "haha";
        String shortBio = "안녕하세요!";
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
