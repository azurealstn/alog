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
    @DisplayName("/posts ????????? ????????? ????????? ?????? ??????")
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
                .content("????????????????")
                .member(savedMember)
                .description("?????????")
                .build();
        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "?????????????????? ????????? ????????? ????????????. (application/json)";

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
                .andExpect(jsonPath("$.validation[0].errorMessage").value("????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts ????????? ????????? ????????? ?????? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void posts_create_api_validate_content() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title("???????????????")
                .content("")
                .member(savedMember)
                .description("?????????")
                .build();
        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "?????????????????? ????????? ????????? ????????????. (application/json)";

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
                .andExpect(jsonPath("$.validation[0].errorMessage").value("????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts ????????? ????????? ????????? ?????? ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void posts_create_api_validate_description() throws Exception {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("member", new SessionMemberDto(savedMember));

        String description = "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????" +
                "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????" +
                "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????" +
                "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title("???????????????")
                .content("???????????????.")
                .member(savedMember)
                .description(description)
                .build();
        String body = objectMapper.writeValueAsString(requestDto);
        String code = "400";
        String message = "?????????????????? ????????? ????????? ????????????. (application/json)";

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
                .andExpect(jsonPath("$.validation[0].errorMessage").value("????????? ?????? ?????? 150??? ?????? ???????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts ????????? ????????? DB ??????")
    @WithMockUser("MEMBER")
    @Transactional
    void create_posts() throws Exception {
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
    @DisplayName("/api/v1/posts/{postsId} ????????? ??? ?????? ?????? ??????")
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
                .description("?????????")
                .build();

        String expectedTitle = "foo";
        String expectedContent = "bar";
        String expectedDescription = "?????????";

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
    @DisplayName("/api/v1/posts/{postsId} ????????? ??? ?????? ?????? ??????")
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
                .description("?????????")
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
    @DisplayName("??? ?????? ????????? ????????? ??????")
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
                        .title("test ?????? - " + (i + 1))
                        .content("?????? ?????? - " + (i + 1))
                        .description("????????? - " + (i + 1))
                        .secret(false)
                        .member(savedMember)
                        .build())
                .collect(Collectors.toList());
        postsRepository.saveAll(collect);

        //expected
        mockMvc.perform(get("/api/v1/auth/posts?page=1&size=9")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(9)))
                .andExpect(jsonPath("$[0].title").value("test ?????? - 30"))
                .andExpect(jsonPath("$[0].content").value("?????? ?????? - 30"))
                .andExpect(jsonPath("$[0].description").value("????????? - 30"))
                .andExpect(jsonPath("$[8].title").value("test ?????? - 22"))
                .andExpect(jsonPath("$[8].content").value("?????? ?????? - 22"))
                .andExpect(jsonPath("$[8].description").value("????????? - 22"))
                .andDo(print());

    }

    @Test
    @DisplayName("??? ?????? ????????? ????????? ??????")
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
                        .title("test ?????? - " + (i + 1))
                        .content("?????? ?????? - " + (i + 1))
                        .description("????????? - " + (i + 1))
                        .secret(false)
                        .member(savedMember)
                        .build())
                .collect(Collectors.toList());
        postsRepository.saveAll(collect);

        //expected
        mockMvc.perform(get("/api/v1/auth/posts?page=2&size=9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(9)))
                .andExpect(jsonPath("$[0].title").value("test ?????? - 21"))
                .andExpect(jsonPath("$[0].content").value("?????? ?????? - 21"))
                .andExpect(jsonPath("$[0].description").value("????????? - 21"))
                .andExpect(jsonPath("$[8].title").value("test ?????? - 13"))
                .andExpect(jsonPath("$[8].content").value("?????? ?????? - 13"))
                .andExpect(jsonPath("$[8].description").value("????????? - 13"))
                .andDo(print());

    }

    @Test
    @DisplayName("?page=1??? ?????? ?page=0?????? ???????????? ??? ???????????? ????????????.")
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
                        .title("test ?????? - " + (i + 1))
                        .content("?????? ?????? - " + (i + 1))
                        .description("????????? - " + (i + 1))
                        .secret(false)
                        .member(savedMember)
                        .build())
                .collect(Collectors.toList());
        postsRepository.saveAll(collect);

        //expected
        mockMvc.perform(get("/api/v1/auth/posts?page=0&size=9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(9)))
                .andExpect(jsonPath("$[0].title").value("test ?????? - 30"))
                .andExpect(jsonPath("$[0].content").value("?????? ?????? - 30"))
                .andExpect(jsonPath("$[0].description").value("????????? - 30"))
                .andExpect(jsonPath("$[8].title").value("test ?????? - 22"))
                .andExpect(jsonPath("$[8].content").value("?????? ?????? - 22"))
                .andExpect(jsonPath("$[8].description").value("????????? - 22"))
                .andDo(print());
    }

    @Test
    @DisplayName("????????? ????????? DB ?????? ??????")
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
                .description("?????????")
                .build();

        Long modifiedId = postsRepository.save(posts).getId();

        String expectedTitle = "???????????????.";
        String expectedContent = "???????????????.";
        String expectedDescription = "??????????????????.";
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
    @DisplayName("????????? ????????? DB ?????? ??????")
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
                .description("?????????")
                .build();

        Long modifiedId = postsRepository.save(posts).getId();

        String expectedTitle = "???????????????.";
        String expectedContent = "???????????????.";
        String expectedDescription = "??????????????????.";
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
    @DisplayName("????????? ????????? DB ?????? ?????? ????????? ?????? ??????")
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
                .description("?????????")
                .build();

        Long modifiedId = postsRepository.save(posts).getId();

        String expectedTitle = "    ";
        String expectedContent = "???????????????.";
        String expectedDescription = "??????????????????.";
        PostsModifyRequestDto modifyRequestDto = PostsModifyRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .description(expectedDescription)
                .build();

        String code = "400";
        String message = "?????????????????? ????????? ????????? ????????????. (application/json)";

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
                .andExpect(jsonPath("$.validation[0].errorMessage").value("????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("????????? ????????? DB ?????? ?????? ????????? ?????? ??????")
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
                .description("?????????")
                .build();

        Long modifiedId = postsRepository.save(posts).getId();

        String expectedTitle = "???????????????.";
        String expectedContent = "     ";
        String expectedDescription = "??????????????????.";
        PostsModifyRequestDto modifyRequestDto = PostsModifyRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .description(expectedDescription)
                .build();

        String code = "400";
        String message = "?????????????????? ????????? ????????? ????????????. (application/json)";

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
                .andExpect(jsonPath("$.validation[0].errorMessage").value("????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("????????? ????????? DB ??????")
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
                .description("?????????")
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
    @DisplayName("????????? ????????? DB ?????? ??????")
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
                .description("?????????")
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
    @DisplayName("???????????? ??? ??????")
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
                        .title("test ?????? - " + (i + 1))
                        .content("?????? ?????? - " + (i + 1))
                        .description("????????? - " + (i + 1))
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
                .andExpect(jsonPath("$[0].title").value("test ?????? - 30"))
                .andExpect(jsonPath("$[0].content").value("?????? ?????? - 30"))
                .andExpect(jsonPath("$[0].description").value("????????? - 30"))
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