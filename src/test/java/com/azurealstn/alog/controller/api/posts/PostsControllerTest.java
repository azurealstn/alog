package com.azurealstn.alog.controller.api.posts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest
class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/posts 요청시 게시글이 등록")
    void posts_create_api() throws Exception {
        String content = "{}";
        assertThatThrownBy(() -> mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect((result) -> Assertions.assertTrue(result.getResolvedException().getClass().isAssignableFrom(Exception.class)))
                .andReturn());

    }


}