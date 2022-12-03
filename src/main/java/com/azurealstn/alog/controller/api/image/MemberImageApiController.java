package com.azurealstn.alog.controller.api.image;

import com.amazonaws.services.s3.AmazonS3;
import com.azurealstn.alog.dto.image.MemberImageResponseDto;
import com.azurealstn.alog.dto.image.PostsImageResponseDto;
import com.azurealstn.alog.service.image.MemberImageService;
import com.azurealstn.alog.service.image.PostsImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class MemberImageApiController {

    private final MemberImageService memberImageService;

    @PostMapping("/api/v1/uploadMemberImageThumbnailSave/{memberId}")
    public MemberImageResponseDto uploadMemberImageThumbnailSave(@RequestParam("image") MultipartFile multipartFile, @PathVariable Long memberId) throws IOException {
        return memberImageService.thumbnailUploadSave(multipartFile, memberId);
    }

    @PostMapping("/api/v1/uploadMemberImageThumbnail")
    public MemberImageResponseDto uploadMemberImageThumbnail(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        return memberImageService.thumbnailUpload(multipartFile);
    }

    @PostMapping("/api/v1/uploadMemberImageThumbnailSaveS3/{memberId}")
    public MemberImageResponseDto uploadMemberImageThumbnailSaveS3(@RequestParam("image") MultipartFile multipartFile, @PathVariable Long memberId) throws IOException {
        return memberImageService.thumbnailUploadSaveS3(multipartFile, memberId);
    }

    @PostMapping("/api/v1/uploadMemberImageThumbnailS3")
    public MemberImageResponseDto uploadMemberImageThumbnailS3(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        return memberImageService.thumbnailUploadS3(multipartFile);
    }

    @GetMapping("/api/v1/auth/images/member/{storeFilename}")
    public Resource displayImage(@PathVariable String storeFilename) throws Exception {
        return memberImageService.displayImage(storeFilename);
    }

    @DeleteMapping("/api/v1/deleteByMemberThumbnail/{memberId}")
    public void deleteByThumbnail(@PathVariable Long memberId) {
        memberImageService.deleteByThumbnail(memberId);
    }
}
