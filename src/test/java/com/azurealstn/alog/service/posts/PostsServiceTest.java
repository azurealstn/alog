package com.azurealstn.alog.service.posts;

import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.repository.posts.PostsRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    @DisplayName("글 목록 첫번째 페이지 조회 서비스")
    void findAll_posts() throws Exception {
        //given
        List<Posts> collect = IntStream.range(0, 30)
                .mapToObj(i -> Posts.builder()
                        .title("test 제목 - " + (i + 1))
                        .content("뭐로 할까 - " + (i + 1))
                        .build())
                .collect(Collectors.toList());
        postsRepository.saveAll(collect);

        Pageable pageable = PageRequest.of(0, 9, Sort.by(Sort.Direction.DESC, "id"));

        //when
        Page<Posts> all = postsRepository.findAll(pageable);

        //then
        assertThat(all.getSize()).isEqualTo(9); //한 페이지당 row 수
        assertThat(all.getTotalElements()).isEqualTo(30); //총 row 수
        assertThat(all.getTotalPages()).isEqualTo(4); //총 페이지 수
        assertThat(all.isFirst()).isTrue(); //첫번째 페이지
        assertThat(all.isLast()).isFalse(); //마지막 페이지
        assertThat(all.hasNext()).isTrue(); //다음 페이지 존재여부
        assertThat(all.hasPrevious()).isFalse(); //이전 페이지 존재여부
    }
}