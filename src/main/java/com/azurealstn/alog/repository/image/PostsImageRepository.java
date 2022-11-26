package com.azurealstn.alog.repository.image;

import com.azurealstn.alog.domain.image.PostsImage;
import com.azurealstn.alog.repository.hashtag.PostsHashTagMapRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsImageRepository extends JpaRepository<PostsImage, Long>, PostsImageRepositoryCustom {
}
