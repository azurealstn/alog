package com.azurealstn.alog.service.posts;

import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.repository.posts.PostsRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostsServiceTest {

    @Autowired
    private PostsService postsService;

    @Autowired
    private PostsRepository postsRepository;

    @BeforeEach
    public void beforeEach() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성 비즈니스 로직 테스트")
    void create_posts_service_test() throws Exception {
        //given
        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postsService.create(requestDto);
        List<Posts> all = postsRepository.findAll();
        Posts posts = all.get(0);

        //then
        assertThat(1L).isEqualTo(postsRepository.count());
        assertThat(1).isEqualTo(all.size());
        assertThat("제목입니다.").isEqualTo(posts.getTitle());
        assertThat("내용입니다.").isEqualTo(posts.getContent());
    }

    @Test
    @DisplayName("글 단건 조회 성공 테스트")
    void findById_posts_o() throws Exception {
        //given
        PostsCreateRequestDto requestDto = PostsCreateRequestDto.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        Long savedId = postsService.create(requestDto);

        //when
        PostsResponseDto findPosts = postsService.findById(savedId);

        //then
        assertThat(1).isEqualTo(postsRepository.count());
        assertThat("제목입니다.").isEqualTo(findPosts.getTitle());
        assertThat("내용입니다.").isEqualTo(findPosts.getContent());
    }

    @Test
    @DisplayName("글 단건 조회 실패 테스트")
    void findById_posts_x() throws Exception {
        //given
        Long postsId = 1L; //글을 작성하지 않은 상태

        //when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> postsService.findById(postsId));

        //then
        assertThat(e.getMessage()).isEqualTo("해당 글이 없습니다. id=" + postsId);
    }

    @Test
    @DisplayName("글 전체 조회 테스트")
    void findAll_posts() throws Exception {
        //given
        PostsCreateRequestDto requestDto1 = PostsCreateRequestDto.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        PostsCreateRequestDto requestDto2 = PostsCreateRequestDto.builder()
                .title("foo")
                .content("bar")
                .build();

        postsService.create(requestDto1);
        postsService.create(requestDto2);

        //when
        List<PostsResponseDto> posts = postsService.findAll();

        //then
        assertThat(2).isEqualTo(posts.size());
    }
}