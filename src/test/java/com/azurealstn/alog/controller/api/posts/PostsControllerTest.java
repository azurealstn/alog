package com.azurealstn.alog.controller.api.posts;

import com.azurealstn.alog.dto.exception.ValidationDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/posts 게시글 등록시 데이터 검증")
    void posts_create_api_validate() throws Exception {
        String body = "{\"title\": \"\", \"content\": \"\"}";
        String code = "400";
        String message = "잘못된 요청입니다.";
        List<ValidationDto> list = new ArrayList<>();
        list.add(new ValidationDto("content", "내용이 비어있습니다."));
        list.add(new ValidationDto("title", "제목이 비어있습니다."));

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.validation").isNotEmpty())
                .andExpect(jsonPath("$.validation[0].fieldName", is(list.get(0).getFieldName())))
                .andExpect(jsonPath("$.validation[0].errorMessage", is(list.get(0).getErrorMessage())))
                .andExpect(jsonPath("$.validation[1].fieldName", is(list.get(1).getFieldName())))
                .andExpect(jsonPath("$.validation[1].errorMessage", is(list.get(1).getErrorMessage())))
                .andDo(print());

    }


}