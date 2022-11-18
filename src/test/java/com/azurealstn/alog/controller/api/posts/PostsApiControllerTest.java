package com.azurealstn.alog.controller.api.posts;

import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsModifyRequestDto;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.repository.posts.PostsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

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

    @BeforeEach
    void beforeEach() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        postsRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        postsRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 게시글 등록시 데이터 검증 제목")
    @WithMockUser("MEMBER")
    @Transactional
    void posts_create_api_validate_title() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title("")
                .content("안녕하세요?")
                .member(savedMember)
                .description("소개글")
                .build();
        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "클라이언트의 잘못된 요청이 있습니다. (application/json)";

        //expected
        mockMvc.perform(post("/api/v1/posts")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation.length()", is(1)))
                .andExpect(jsonPath("$.validation").isNotEmpty())
                .andExpect(jsonPath("$.validation[0].fieldName").value("title"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("제목이 비어있습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 게시글 등록시 데이터 검증 내용")
    @WithMockUser("MEMBER")
    @Transactional
    void posts_create_api_validate_content() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title("안녕하세요")
                .content("")
                .member(savedMember)
                .description("소개글")
                .build();
        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "클라이언트의 잘못된 요청이 있습니다. (application/json)";

        //expected
        mockMvc.perform(post("/api/v1/posts")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation.length()", is(1)))
                .andExpect(jsonPath("$.validation").isNotEmpty())
                .andExpect(jsonPath("$.validation[0].fieldName").value("content"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("내용이 비어있습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 게시글 등록시 데이터 검증 소개")
    @WithMockUser("MEMBER")
    @Transactional
    void posts_create_api_validate_description() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        String description = "글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯" +
                "글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯" +
                "글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯" +
                "글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯글자가다섯";
        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title("안녕하세요")
                .content("제목입니다.")
                .member(savedMember)
                .description(description)
                .build();
        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "클라이언트의 잘못된 요청이 있습니다. (application/json)";

        //expected
        mockMvc.perform(post("/api/v1/posts")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation.length()", is(1)))
                .andExpect(jsonPath("$.validation").isNotEmpty())
                .andExpect(jsonPath("$.validation[0].fieldName").value("description"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("포스트 소개 글을 150자 내로 적어주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 게시글 등록시 DB 저장")
    @WithMockUser("MEMBER")
    @Transactional
    void create_posts() throws Exception {
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
                .description(description)
                .build();

        //expected
        mockMvc.perform(post("/api/v1/posts")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("/api/v1/posts/{postsId} 요청시 글 단건 조회 성공")
    @WithMockUser("MEMBER")
    @Transactional
    void select_posts_o() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .description("소개글")
                .build();

        String expectedTitle = "foo";
        String expectedContent = "bar";
        String expectedDescription = "소개글";

        //when
        Long savedId = postsRepository.save(posts).getId();

        //then
        mockMvc.perform(get("/api/v1/auth/posts-data/{postsId}", savedId)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedId))
                .andExpect(jsonPath("$.title", is(expectedTitle)))
                .andExpect(jsonPath("$.content", is(expectedContent)))
                .andExpect(jsonPath("$.description", is(expectedDescription)))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/v1/posts/{postsId} 요청시 글 단건 조회 실패")
    @WithMockUser("MEMBER")
    @Transactional
    void select_posts_x() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .description("소개글")
                .build();
        Long savedId = postsRepository.save(posts).getId();

        //expected
        mockMvc.perform(get("/api/v1/auth/posts-data/{postsId}", savedId + 1)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("글 목록 첫번째 페이지 조회")
    @WithMockUser("MEMBER")
    @Transactional
    void findAll_posts_first_page() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        List<Posts> collect = IntStream.range(0, 30)
                .mapToObj(i -> Posts.builder()
                        .title("test 제목 - " + (i + 1))
                        .content("뭐로 할까 - " + (i + 1))
                        .description("소개글 - " + (i + 1))
                        .member(savedMember)
                        .build())
                .collect(Collectors.toList());
        postsRepository.saveAll(collect);

        //expected
        mockMvc.perform(get("/api/v1/posts?page=1&size=9")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(9)))
                .andExpect(jsonPath("$[0].title").value("test 제목 - 30"))
                .andExpect(jsonPath("$[0].content").value("뭐로 할까 - 30"))
                .andExpect(jsonPath("$[0].description").value("소개글 - 30"))
                .andExpect(jsonPath("$[8].title").value("test 제목 - 22"))
                .andExpect(jsonPath("$[8].content").value("뭐로 할까 - 22"))
                .andExpect(jsonPath("$[8].description").value("소개글 - 22"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 목록 두번째 페이지 조회")
    @WithMockUser("MEMBER")
    @Transactional
    void findAll_posts_second_page() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        List<Posts> collect = IntStream.range(0, 30)
                .mapToObj(i -> Posts.builder()
                        .title("test 제목 - " + (i + 1))
                        .content("뭐로 할까 - " + (i + 1))
                        .description("소개글 - " + (i + 1))
                        .member(savedMember)
                        .build())
                .collect(Collectors.toList());
        postsRepository.saveAll(collect);

        //expected
        mockMvc.perform(get("/api/v1/posts?page=2&size=9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(9)))
                .andExpect(jsonPath("$[0].title").value("test 제목 - 21"))
                .andExpect(jsonPath("$[0].content").value("뭐로 할까 - 21"))
                .andExpect(jsonPath("$[0].description").value("소개글 - 21"))
                .andExpect(jsonPath("$[8].title").value("test 제목 - 13"))
                .andExpect(jsonPath("$[8].content").value("뭐로 할까 - 13"))
                .andExpect(jsonPath("$[8].description").value("소개글 - 13"))
                .andDo(print());

    }

    @Test
    @DisplayName("?page=1이 아닌 ?page=0으로 요청해도 첫 페이지를 가져온다.")
    @WithMockUser("MEMBER")
    @Transactional
    void first_page_zero() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        List<Posts> collect = IntStream.range(0, 30)
                .mapToObj(i -> Posts.builder()
                        .title("test 제목 - " + (i + 1))
                        .content("뭐로 할까 - " + (i + 1))
                        .description("소개글 - " + (i + 1))
                        .member(savedMember)
                        .build())
                .collect(Collectors.toList());
        postsRepository.saveAll(collect);

        //expected
        mockMvc.perform(get("/api/v1/posts?page=0&size=9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(9)))
                .andExpect(jsonPath("$[0].title").value("test 제목 - 30"))
                .andExpect(jsonPath("$[0].content").value("뭐로 할까 - 30"))
                .andExpect(jsonPath("$[0].description").value("소개글 - 30"))
                .andExpect(jsonPath("$[8].title").value("test 제목 - 22"))
                .andExpect(jsonPath("$[8].content").value("뭐로 할까 - 22"))
                .andExpect(jsonPath("$[8].description").value("소개글 - 22"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정시 DB 수정 성공")
    @WithMockUser("MEMBER")
    @Transactional
    void modify_posts_api_o() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .description("소개글")
                .build();

        Long modifiedId = postsRepository.save(posts).getId();

        String expectedTitle = "제목입니다.";
        String expectedContent = "내용입니다.";
        String expectedDescription = "소개글입니다.";
        PostsModifyRequestDto modifyRequestDto = PostsModifyRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .description(expectedDescription)
                .build();

        //expected
        mockMvc.perform(put("/api/v1/posts/{posts_id}", modifiedId)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정시 DB 수정 실패")
    @WithMockUser("MEMBER")
    @Transactional
    void modify_posts_api_x() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .description("소개글")
                .build();

        Long modifiedId = postsRepository.save(posts).getId();

        String expectedTitle = "제목입니다.";
        String expectedContent = "내용입니다.";
        String expectedDescription = "소개글입니다.";
        PostsModifyRequestDto modifyRequestDto = PostsModifyRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .description(expectedDescription)
                .build();

        //expected
        mockMvc.perform(put("/api/v1/posts/{postsId}", modifiedId + 1)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정시 DB 수정 실패 데이터 검증 제목")
    @WithMockUser("MEMBER")
    @Transactional
    void modify_posts_api_x_title() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .description("소개글")
                .build();

        Long modifiedId = postsRepository.save(posts).getId();

        String expectedTitle = "    ";
        String expectedContent = "내용입니다.";
        String expectedDescription = "소개글입니다.";
        PostsModifyRequestDto modifyRequestDto = PostsModifyRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .description(expectedDescription)
                .build();

        String code = "400";
        String message = "클라이언트의 잘못된 요청이 있습니다. (application/json)";

        //expected
        mockMvc.perform(put("/api/v1/posts/{postsId}", modifiedId)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation.length()", is(1)))
                .andExpect(jsonPath("$.validation").isNotEmpty())
                .andExpect(jsonPath("$.validation[0].fieldName").value("title"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("제목이 비어있습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정시 DB 수정 실패 데이터 검증 내용")
    @WithMockUser("MEMBER")
    @Transactional
    void modify_posts_api_x_content() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .description("소개글")
                .build();

        Long modifiedId = postsRepository.save(posts).getId();

        String expectedTitle = "제목입니다.";
        String expectedContent = "     ";
        String expectedDescription = "소개글입니다.";
        PostsModifyRequestDto modifyRequestDto = PostsModifyRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .description(expectedDescription)
                .build();

        String code = "400";
        String message = "클라이언트의 잘못된 요청이 있습니다. (application/json)";

        //expected
        mockMvc.perform(put("/api/v1/posts/{postsId}", modifiedId)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation.length()", is(1)))
                .andExpect(jsonPath("$.validation").isNotEmpty())
                .andExpect(jsonPath("$.validation[0].fieldName").value("content"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value("내용이 비어있습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제시 DB 삭제")
    @WithMockUser("MEMBER")
    @Transactional
    void delete_posts_api() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .description("소개글")
                .build();

        Long deletedId = postsRepository.save(posts).getId();

        //expected
        mockMvc.perform(delete("/api/v1/posts/{posts_id}", deletedId)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제시 DB 삭제 실패")
    @WithMockUser("MEMBER")
    @Transactional
    void delete_posts_api_x() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .description("소개글")
                .build();

        Long deletedId = postsRepository.save(posts).getId();

        //expected
        mockMvc.perform(delete("/api/v1/posts/{postsId}", deletedId + 1)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("회원별로 글 목록")
    @WithMockUser("MEMBER")
    @Transactional
    void findAll_posts_by_member() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        List<Posts> collect = IntStream.range(0, 30)
                .mapToObj(i -> Posts.builder()
                        .title("test 제목 - " + (i + 1))
                        .content("뭐로 할까 - " + (i + 1))
                        .description("소개글 - " + (i + 1))
                        .member(savedMember)
                        .build())
                .collect(Collectors.toList());
        postsRepository.saveAll(collect);

        //expected
        mockMvc.perform(get("/api/v1/posts/by-member")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(30)))
                .andExpect(jsonPath("$[0].title").value("test 제목 - 30"))
                .andExpect(jsonPath("$[0].content").value("뭐로 할까 - 30"))
                .andExpect(jsonPath("$[0].description").value("소개글 - 30"))
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