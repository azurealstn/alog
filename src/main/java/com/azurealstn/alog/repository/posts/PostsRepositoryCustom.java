package com.azurealstn.alog.repository.posts;

import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.hashtag.HashTagSearchDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;

import java.util.List;

public interface PostsRepositoryCustom {

    List<Posts> findAll(PostsSearchDto searchDto);

    int findAllCount();

    List<Posts> findAllByMember(Long memberId);

    List<Posts> findAllJoinWithHashTag(String name, HashTagSearchDto searchDto);

    int findAllJoinWithHashTagCount(String name);

}
