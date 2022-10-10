package com.azurealstn.alog.dto.posts;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PostsSearchDtoTest {

    @Test
    @DisplayName("offset 검증")
    void validate_offset() {
        //given
        PostsSearchDto searchDto1 = PostsSearchDto.builder() //page=0
                .page(0)
                .size(9)
                .build();

        PostsSearchDto searchDto2 = PostsSearchDto.builder() //page=1
                .page(1)
                .size(9)
                .build();

        PostsSearchDto searchDto3 = PostsSearchDto.builder() //page=2
                .page(2)
                .size(9)
                .build();

        //expected
        assertThat(searchDto1.getOffset()).isEqualTo(0);
        assertThat(searchDto2.getOffset()).isEqualTo(0);
        assertThat(searchDto3.getOffset()).isEqualTo(9L);
    }
}