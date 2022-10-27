package com.azurealstn.alog.controller.api.hello;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(roles = "MEMBER")
@WebMvcTest(controllers = HelloApiController.class)
class HelloApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/hello 요청시 문자열 hello를 출력")
    void hello_test() throws Exception {
        String hello = "hello";

        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello))
                .andDo(print());
    }

    @Test
    @DisplayName("/helloDto 요청시 요청 파라미터 데이터 검증")
    void helloDto_test() throws Exception {
        String name = "john";
        int age = 42;

        mockMvc.perform(get("/helloDto")
                        .param("name", name)
                        .param("age", String.valueOf(age)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.age", is(age)));
    }
}