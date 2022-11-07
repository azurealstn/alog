package com.azurealstn.alog.repository.posts;

import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.posts.PostsSearchDto;

import java.util.List;

public interface PostsRepositoryCustom {

    List<Posts> findAll(PostsSearchDto searchDto);

    int findAllCount();
}
