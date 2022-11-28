package com.azurealstn.alog.controller.api.image;

import com.azurealstn.alog.domain.image.PostsImage;
import com.azurealstn.alog.dto.image.PostsImageResponseDto;
import com.azurealstn.alog.service.image.PostsImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class PostsImageApiController {

    private final PostsImageService postsImageService;

    @PostMapping("/api/v1/uploadPostImage")
    public PostsImageResponseDto uploadPostImage(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        return postsImageService.fileUpload(multipartFile);
    }

    @PostMapping("/api/v1/uploadPostImageThumbnail/{postsId}")
    public PostsImageResponseDto uploadPostImageThumbnailSave(@RequestParam("image") MultipartFile multipartFile, @PathVariable Long postsId) throws IOException {
        return postsImageService.thumbnailUploadSave(multipartFile, postsId);
    }

    @PostMapping("/api/v1/uploadPostImageThumbnail")
    public PostsImageResponseDto uploadPostImageThumbnail(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        return postsImageService.thumbnailUpload(multipartFile);
    }

    @GetMapping("/api/v1/auth/images/{storeFilename}")
    public Resource displayImage(@PathVariable String storeFilename) throws Exception {
        return postsImageService.displayImage(storeFilename);
    }

    @DeleteMapping("/api/v1/deleteByThumbnail/{postsId}")
    public void deleteByThumbnail(@PathVariable Long postsId) {
        postsImageService.deleteByThumbnail(postsId);
    }
}
