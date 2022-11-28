package com.azurealstn.alog.service.image;

import com.azurealstn.alog.Infra.exception.image.PostsImageNotFound;
import com.azurealstn.alog.Infra.utils.FileUtils;
import com.azurealstn.alog.domain.image.PostsImage;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.image.PostsImageResponseDto;
import com.azurealstn.alog.repository.image.PostsImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsImageService {

    private final FileUtils fileUtils;
    private final PostsImageRepository postsImageRepository;

    @Transactional
    public PostsImageResponseDto fileUpload(MultipartFile multipartFile) throws IOException {
        PostsImage postsImage = fileUtils.storePostsImageFile(multipartFile);
        return new PostsImageResponseDto(postsImage);
    }

    @Transactional
    public Resource displayImage(String storeFilename) throws Exception {
        return new UrlResource("file:" + fileUtils.getFullPath(storeFilename));
    }

    @Transactional
    public PostsImageResponseDto thumbnailUpload(MultipartFile multipartFile) throws IOException {
        PostsImage postsImage = fileUtils.storePostsImageThumbnail(multipartFile);
        return new PostsImageResponseDto(postsImage);
    }

    @Transactional
    public PostsImageResponseDto thumbnailUploadSave(MultipartFile multipartFile, Long postsId) throws IOException {
        PostsImage postsImage = fileUtils.storePostsImageThumbnailSave(multipartFile, postsId);
        Optional<PostsImage> existsPostsImage = postsImageRepository.findThumbnailByPosts(postsId);
        existsPostsImage.ifPresent(postsImageRepository::delete);
        postsImageRepository.save(postsImage);

        return new PostsImageResponseDto(postsImage);
    }

    @Transactional(readOnly = true)
    public PostsImageResponseDto findThumbnailByPosts(Long postsId) {
        Optional<PostsImage> postsImage = postsImageRepository.findThumbnailByPosts(postsId);
        if (postsImage.isPresent()) {
            return new PostsImageResponseDto(postsImage.get());
        } else {
            return null;
        }
    }

    @Transactional
    public void deleteByThumbnail(Long postsId) {
        postsImageRepository.deleteByThumbnail(postsId);
    }
}
