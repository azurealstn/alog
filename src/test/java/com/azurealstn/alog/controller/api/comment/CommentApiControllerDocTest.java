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
    @DisplayName("댓글 작성")
    @WithMockUser("MEMBER")
    @Transactional
    void create_comment_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        String title = "제목입니다.";
        String content = "내용입니다.";
        String description = "소개입니다.";
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
                .content("댓글입니다.")
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
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("level").type(JsonFieldType.NUMBER).description("댓글 레벨"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("postsId").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("member").type(JsonFieldType.NULL).description("회원"),
                                fieldWithPath("posts").type(JsonFieldType.NULL).description("게시글"),
                                fieldWithPath("upCommentId").type(JsonFieldType.NULL).description("상위 댓글")

                        )
                ));

    }

    @Test
    @DisplayName("댓글 수정")
    @WithMockUser("MEMBER")
    @Transactional
    void modify_comment_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        String title = "제목입니다.";
        String content = "내용입니다.";
        String description = "소개입니다.";
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
                .content("댓글입니다.")
                .level(1)
                .memberId(member.getId())
                .postsId(posts.getId())
                .build();
        Long commentId = commentRepository.save(commentCreateRequestDto.toEntity()).getId();

        CommentModifyRequestDto modifyRequestDto = CommentModifyRequestDto.builder()
                .content("수정한 댓글입니다.")
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
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").description("댓글 내용")
                        )
                ));
    }

    @Test
    @DisplayName("댓글 삭제")
    @WithMockUser("MEMBER")
    @Transactional
    void delete_comment_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        String title = "제목입니다.";
        String content = "내용입니다.";
        String description = "소개입니다.";
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
                .content("댓글입니다.")
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
                                parameterWithName("commentId").description("댓글 ID")
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
