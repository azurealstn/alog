package com.azurealstn.alog.dto.image;

import com.azurealstn.alog.domain.image.PostsImage;
import com.azurealstn.alog.domain.posts.Posts;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
public class PostsImageResponseDto implements Serializable {

    private Long id;
    private Posts posts;
    private String originalFilename; //사용자가 업로드한 파일 이름
    private String storeFilename; //서버 내부에서 관리하는 파일명
    private String imageUrl;
    private Boolean thumbnailYn;

    public PostsImageResponseDto(PostsImage entity) {
        this.id = entity.getId();
        this.posts = entity.getPosts();
        this.originalFilename = entity.getOriginalFilename();
        this.storeFilename = entity.getStoreFilename();
        this.imageUrl = entity.getImageUrl();
        this.thumbnailYn = entity.getThumbnailYn();
    }


}
