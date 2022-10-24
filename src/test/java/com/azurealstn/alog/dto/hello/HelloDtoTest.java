package com.azurealstn.alog.dto.hello;

import com.azurealstn.alog.dto.posts.hello.HelloDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloDtoTest {

    @Test
    @DisplayName("lombok이 제대로 동작하는지 테스트")
    void lombok_test() {
        //given
        String name = "mike";
        int age = 30;

        //when
        HelloDto helloDto = new HelloDto(name, age);

        //then
        assertThat(helloDto.getName()).isEqualTo(name);
        assertThat(helloDto.getAge()).isEqualTo(age);
    }

}