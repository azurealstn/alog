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
    @DisplayName("???????????? ?????? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void findById_api() throws Exception {
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

        mockMvc.perform(get("/api/v1/temp-save/{tempSaveId}", savedId)
                        .session(mockHttpSession)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("tempsave-findById", pathParameters(
                                parameterWithName("tempSaveId").description("???????????? ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("???????????? ID"),
                                fieldWithPath("title").description("???????????? ??????"),
                                fieldWithPath("content").description("???????????? ??????"),
                                fieldWithPath("tempCode").description("???????????? ??????"),
                                fieldWithPath("previousTime").description("???????????? ?????? ????????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("???????????? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void create_tempsave_api() throws Exception {
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

        //expected
        mockMvc.perform(post("/api/v1/temp-save")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("tempsave-create",
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("???????????? ??????"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("???????????? ??????"),
                                fieldWithPath("tempCode").type(JsonFieldType.STRING).description("???????????? ??????"),
                                fieldWithPath("member").type(JsonFieldType.OBJECT).description("?????? ??????"),
                                fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("member.email").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("member.name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("member.picture").type(JsonFieldType.STRING).description("?????? ???????????????"),
                                fieldWithPath("member.role").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("member.emailAuth").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                                fieldWithPath("member.username").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("member.shortBio").type(JsonFieldType.STRING).description("?????? ??? ??? ??????"),
                                fieldWithPath("member.roleKey").type(JsonFieldType.STRING).description("?????? ?????? ???"),
                                fieldWithPath("member.createdDate").type(JsonFieldType.STRING).description("?????? ????????????"),
                                fieldWithPath("member.modifiedDate").type(JsonFieldType.STRING).description("?????? ????????????")

                        )
                ));

    }

    @Test
    @DisplayName("???????????? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void modify_tempsave_api() throws Exception {
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
                .andDo(print())
                .andDo(document("tempsave-modify",
                        pathParameters(
                                parameterWithName("tempCode").description("???????????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("???????????? ??????"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("???????????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("???????????? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void delete_tempsave_api() throws Exception {
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
                .andDo(print())
                .andDo(document("tempsave-delete",
                        pathParameters(
                                parameterWithName("tempSaveId").description("???????????? ID")
                        )
                ));
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
