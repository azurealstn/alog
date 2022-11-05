package com.azurealstn.alog.repository.posts;

import com.azurealstn.alog.domain.posts.Posts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@WithMockUser(roles = "MEMBER")
@SpringBootTest
class PostsRepositoryTest {

    @Autowired
    private PostsRepository postsRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    @DisplayName("JPA Auditing 활성화")
    @Transactional
    void baseTimeEntity_work() {
        //given
        LocalDateTime now = LocalDateTime.of(2022, 10, 10, 0, 0, 0);
        Posts posts = Posts.builder()
                .title("title")
                .content("content")
                .description("description")
                .build();
        postsRepository.save(posts);

        //when
        List<Posts> postsList = postsRepository.findAll();
        Posts postsGetFirst = postsList.get(0);

        //then
        assertThat(postsGetFirst.getCreatedDate()).isAfter(now);
        assertThat(postsGetFirst.getModifiedDate()).isAfter(now);


    }
}