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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

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
    @DisplayName("댓글 등록시 DB 저장")
    @WithMockUser("MEMBER")
    @Transactional
    void create_comment() throws Exception {
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
                        .content(objectMapper.writeValueAsString(commentCreateRequestDto)))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 수정시 DB 수정 성공")
    @WithMockUser("MEMBER")
    @Transactional
    void modify_comment_api_o() throws Exception {
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
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제시 DB 삭제")
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
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
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