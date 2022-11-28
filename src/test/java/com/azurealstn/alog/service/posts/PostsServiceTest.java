package com.azurealstn.alog.service.posts;

import com.azurealstn.alog.Infra.exception.posts.PostsNotFound;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.dto.posts.PostsModifyRequestDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.repository.posts.PostsRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@WithMockUser(roles = "MEMBER")
@SpringBootTest
class PostsServiceTest {

    @Autowired
    private PostsService postsService;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HttpSession httpSession;

    @BeforeEach
    public void beforeEach() throws Exception {
        postsRepository.deleteAll();
        memberRepository.deleteAll();
        httpSession.invalidate();
    }

    @Test
    @DisplayName("글 작성 비즈니스 로직 테스트")
    @Transactional
    void create_posts_service_test() throws Exception {
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

        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .member(savedMember)
                .description("글을 소개합니다.")
                .build();

        //when
        postsService.create(requestDto);
        List<Posts> all = postsRepository.findAll();
        Posts posts = all.get(0);

        //then
        assertThat(1L).isEqualTo(postsRepository.count());
        assertThat(1).isEqualTo(all.size());
        assertThat(posts.getTitle()).isEqualTo("제목입니다.");
        assertThat(posts.getContent()).isEqualTo("내용입니다.");
        assertThat(posts.getMember().getEmail()).isEqualTo(email);
        assertThat(posts.getMember().getUsername()).isEqualTo(username);
        assertThat(posts.getDescription()).isEqualTo("글을 소개합니다.");
    }

    @Test
    @DisplayName("글 단건 조회 성공 테스트")
    @Transactional
    void findById_posts_o() throws Exception {
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

        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .member(savedMember)
                .description("소개글입니다.")
                .build();
        Long savedId = postsService.create(requestDto);

        //when
        PostsResponseDto findPosts = postsService.findById(savedId);

        //then
        assertThat(1).isEqualTo(postsRepository.count());
        assertThat(findPosts.getTitle()).isEqualTo("제목입니다.");
        assertThat(findPosts.getContent()).isEqualTo("내용입니다.");
        assertThat(findPosts.getDescription()).isEqualTo("소개글입니다.");
        assertThat(findPosts.getMember().getEmail()).isEqualTo(email);
        assertThat(findPosts.getMember().getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("글 단건 조회 실패 테스트")
    void findById_posts_x() throws Exception {
        //given
        Long postsId = 1L; //글을 작성하지 않은 상태

        //expected
        assertThrows(PostsNotFound.class, () -> postsService.findById(postsId));

    }

    @Test
    @DisplayName("글 목록 첫번째 페이지 조회 서비스")
    @Transactional
    void findAll_posts() throws Exception {
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

        List<Posts> collect = IntStream.range(0, 30)
                .mapToObj(i -> Posts.builder()
                        .title("test 제목 - " + (i + 1))
                        .content("뭐로 할까 - " + (i + 1))
                        .member(savedMember)
                        .description("소개글 - " + (i + 1))
                        .build())
                .collect(Collectors.toList());
        postsRepository.saveAll(collect);

        PostsSearchDto searchDto = new PostsSearchDto(1, 9);

        //when
        List<Posts> postsList = postsRepository.findAll(searchDto);

        //then
        assertThat(postsList.size()).isEqualTo(9);
        assertThat(postsList.get(0).getTitle()).isEqualTo("test 제목 - 30");
        assertThat(postsList.get(8).getTitle()).isEqualTo("test 제목 - 22");
        assertThat(postsList.get(0).getContent()).isEqualTo("뭐로 할까 - 30");
        assertThat(postsList.get(8).getContent()).isEqualTo("뭐로 할까 - 22");
        assertThat(postsList.get(0).getDescription()).isEqualTo("소개글 - 30");
        assertThat(postsList.get(8).getDescription()).isEqualTo("소개글 - 22");
        assertThat(postsList.get(0).getMember().getEmail()).isEqualTo(email);
        assertThat(postsList.get(8).getMember().getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("글 수정 로직 성공 테스트")
    @Transactional
    void modify_posts_o() {
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

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .description("소개글")
                .build();
        postsRepository.save(posts);

        //when
        PostsModifyRequestDto requestDto = PostsModifyRequestDto.builder()
                .title("foo 수정 제목")
                .content("bar 수정 내용")
                .description("소개글 수정")
                .build();
        Long modifiedId = postsService.modify(posts.getId(), requestDto);
        PostsResponseDto responseDto = postsService.findById(modifiedId);

        //then
        assertThat(responseDto.getTitle()).isEqualTo("foo 수정 제목");
        assertThat(responseDto.getContent()).isEqualTo("bar 수정 내용");
        assertThat(responseDto.getDescription()).isEqualTo("소개글 수정");
        assertThat(responseDto.getMember().getEmail()).isEqualTo(email);
        assertThat(responseDto.getMember().getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("글 수정 로직 실패 테스트")
    @Transactional
    void modify_posts_x() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());
        httpSession.setAttribute("member", new SessionMemberDto(savedMember));

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .description("소개글")
                .build();
        postsRepository.save(posts);

        //expected
        PostsModifyRequestDto requestDto = PostsModifyRequestDto.builder()
                .title("foo 수정 제목")
                .content("bar 수정 내용")
                .description("소개글 수정")
                .build();

        assertThrows(PostsNotFound.class, () -> postsService.modify(posts.getId() + 1, requestDto));

    }

    @Test
    @DisplayName("글 삭제 로직 성공 테스트")
    @Transactional
    void delete_posts_o() throws IOException {
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

        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .member(savedMember)
                .description("소개글")
                .build();
        Long deletedId = postsService.create(requestDto);

        //when
        postsService.delete(deletedId);

        //then
        assertThat(postsRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("글 삭제 로직 실패 테스트")
    @Transactional
    void delete_posts_x() throws IOException {
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

        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .member(savedMember)
                .description("소개글")
                .build();
        Long deletedId = postsService.create(requestDto);

        //expected
        assertThrows(PostsNotFound.class, () -> postsService.delete(deletedId + 1));

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

    @Test
    @DisplayName("이전 페이지 없음")
    void prev_page_X() {
        //given
        String url = "/";
        httpSession.setAttribute("prevUrl", url);

        //when
        String prevPageUrl = (String) httpSession.getAttribute("prevUrl");

        //then
        assertThat(prevPageUrl).isEqualTo("/");
    }

    @Test
    @DisplayName("글을 수정이나 삭제할 수 있는 권한이 있는 사람인지 체크")
    void isAuthenticated() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = getMemberCreateRequestDto();

        Member savedMember = memberRepository.save(memberCreateRequestDto.toEntity());
        httpSession.setAttribute("member", new SessionMemberDto(savedMember));

        Posts posts = Posts.builder()
                .title("foo")
                .content("bar")
                .member(savedMember)
                .description("소개글")
                .build();
        postsRepository.save(posts);
        PostsResponseDto postsResponseDto = new PostsResponseDto(posts);

        //when
        SessionMemberDto member = (SessionMemberDto) httpSession.getAttribute("member");

        //then
        assertThat(postsService.isAuthenticated(member, postsResponseDto)).isTrue();
    }

}