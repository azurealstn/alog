package com.azurealstn.alog.repository.image;

import com.azurealstn.alog.domain.image.PostsImage;

import java.util.Optional;

public interface PostsImageRepositoryCustom {

    Optional<PostsImage> findThumbnailByPosts(Long postsId);

    void deleteByThumbnail(Long postsId);
}
