package com.azurealstn.alog.repository.hashtag;

import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.dto.hashtag.HashTagSearchDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;

import java.util.List;

public interface HashTagRepositoryCustom {

    List<HashTag> findByTags(Long postsId);

    List<HashTag> findByTagsName(String name, HashTagSearchDto searchDto);

}
