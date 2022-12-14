package com.azurealstn.alog.controller.api.comment;

import com.azurealstn.alog.Infra.exception.member.MemberNotFound;
import com.azurealstn.alog.Infra.exception.posts.PostsNotFound;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.comment.CommentCreateRequestDto;
import com.azurealstn.alog.dto.comment.CommentModifyRequestDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsModifyRequestDto;
import com.azurealstn.alog.repository.comment.CommentRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.repository.posts.PostsRepository;
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
public class CommentApiControllerDocTest {

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
    private MemberRepository memberRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        commentRepository.deleteAll();
        postsRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        commentRepository.deleteAll();
        postsRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("?????? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void create_comment_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        String title = "???????????????.";
        String content = "???????????????.";
        String description = "???????????????.";
        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title(title)
                .content(content)
                .member(savedMember)
                .secret(false)
                .description(description)
                .build();

        Long postsId = postsRepository.save(requestDto.toEntity()).getId();

        Member member = memberRepository.findById(savedMember.getId())
                .orElseThrow(() -> new MemberNotFound());
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new PostsNotFound());

        CommentCreateRequestDto commentCreateRequestDto = CommentCreateRequestDto.builder()
                .content("???????????????.")
                .level(1)
                .memberId(member.getId())
                .postsId(posts.getId())
                .build();

        //expected
        mockMvc.perform(post("/api/v1/comment")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreateRequestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment-create",
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("level").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("postsId").type(JsonFieldType.NUMBER).description("????????? ID"),
                                fieldWithPath("member").type(JsonFieldType.NULL).description("??????"),
                                fieldWithPath("posts").type(JsonFieldType.NULL).description("?????????"),
                                fieldWithPath("upCommentId").type(JsonFieldType.NULL).description("?????? ??????")

                        )
                ));

    }

    @Test
    @DisplayName("?????? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void modify_comment_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        String title = "???????????????.";
        String content = "???????????????.";
        String description = "???????????????.";
        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title(title)
                .content(content)
                .member(savedMember)
                .secret(false)
                .description(description)
                .build();

        Long postsId = postsRepository.save(requestDto.toEntity()).getId();

        Member member = memberRepository.findById(savedMember.getId())
                .orElseThrow(() -> new MemberNotFound());
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new PostsNotFound());

        CommentCreateRequestDto commentCreateRequestDto = CommentCreateRequestDto.builder()
                .content("???????????????.")
                .level(1)
                .memberId(member.getId())
                .postsId(posts.getId())
                .build();
        Long commentId = commentRepository.save(commentCreateRequestDto.toEntity()).getId();

        CommentModifyRequestDto modifyRequestDto = CommentModifyRequestDto.builder()
                .content("????????? ???????????????.")
                .build();

        //expected
        mockMvc.perform(patch("/api/v1/comment/{commentId}", commentId)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment-modify",
                        pathParameters(
                                parameterWithName("commentId").description("?????? ID")
                        ),
                        requestFields(
                                fieldWithPath("content").description("?????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void delete_comment_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        String title = "???????????????.";
        String content = "???????????????.";
        String description = "???????????????.";
        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title(title)
                .content(content)
                .member(savedMember)
                .secret(false)
                .description(description)
                .build();

        Long postsId = postsRepository.save(requestDto.toEntity()).getId();

        Member member = memberRepository.findById(savedMember.getId())
                .orElseThrow(() -> new MemberNotFound());
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new PostsNotFound());

        CommentCreateRequestDto commentCreateRequestDto = CommentCreateRequestDto.builder()
                .content("???????????????.")
                .level(1)
                .memberId(member.getId())
                .postsId(posts.getId())
                .build();
        Long commentId = commentRepository.save(commentCreateRequestDto.toEntity()).getId();

        //expected
        mockMvc.perform(delete("/api/v1/comment/{commentId}", commentId)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment-delete",
                        pathParameters(
                                parameterWithName("commentId").description("?????? ID")
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
