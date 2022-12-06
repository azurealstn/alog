package com.azurealstn.alog.Infra.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.azurealstn.alog.Infra.exception.image.ImageExtAllowed;
import com.azurealstn.alog.Infra.exception.member.MemberNotFound;
import com.azurealstn.alog.Infra.exception.posts.PostsNotFound;
import com.azurealstn.alog.domain.image.MemberImage;
import com.azurealstn.alog.domain.image.PostsImage;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.image.PostsImageResponseDto;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.repository.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileUtils {

    private final String[] ALLOWED_EXT = {
            "jpg", "jpeg", "jpe", "png",
            "JPG", "JPEG", "JPE", "PNG"
    };

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${upload.path}")
    private String uploadPath;

    private final PostsRepository postsRepository;
    private final MemberRepository memberRepository;
    private final AmazonS3 amazonS3;

    //이미지 파일 full path
    public String getFullPath(String filename) {
        return uploadPath + filename;
    }

    //게시글 이미지 여러개 저장
    public List<PostsImage> storeFileList(List<MultipartFile> multipartFileList) throws IOException {
        List<PostsImage> storeFileList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFileList) {
            if (!multipartFile.isEmpty()) {
                PostsImage postsImage = storePostsImageFile(multipartFile);
                storeFileList.add(postsImage);
            }
        }
        return storeFileList;
    }

    public boolean isImageExtAllowed(String originalFilename) {
        String ext = extractExt(originalFilename);
        List<String> allowedExtList = Arrays.asList(ALLOWED_EXT);
        if (allowedExtList.contains(ext)) {
            return true;
        } else {
            return false;
        }
    }

    //게시글 이미지 저장
    public PostsImage storePostsImageFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFilename)));

        PostsImage postsImage = PostsImage.builder()
                .originalFilename(originalFilename)
                .storeFilename(storeFilename)
                .imageUrl(getFullPath(storeFilename))
                .thumbnailYn(false)
                .build();

        return postsImage;
    }

    public PostsImage storePostsImageFileS3(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);

        if (!isImageExtAllowed(originalFilename)) {
            throw new ImageExtAllowed();
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getInputStream().available());
        objectMetadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, storeFilename, multipartFile.getInputStream(), objectMetadata);

        return PostsImage.builder()
                .originalFilename(originalFilename)
                .storeFilename(storeFilename)
                .imageUrl(amazonS3.getUrl(bucket, storeFilename).toString())
                .thumbnailYn(false)
                .build();
    }

    public PostsImage storePostsImageThumbnail(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFilename)));

        PostsImage postsImage = PostsImage.builder()
                .originalFilename(originalFilename)
                .storeFilename(storeFilename)
                .imageUrl(getFullPath(storeFilename))
                .thumbnailYn(true)
                .build();

        return postsImage;
    }

    public PostsImage storePostsImageThumbnailS3(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);

        if (!isImageExtAllowed(originalFilename)) {
            throw new ImageExtAllowed();
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getInputStream().available());
        objectMetadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, storeFilename, multipartFile.getInputStream(), objectMetadata);

        return PostsImage.builder()
                .originalFilename(originalFilename)
                .storeFilename(storeFilename)
                .imageUrl(amazonS3.getUrl(bucket, storeFilename).toString())
                .thumbnailYn(true)
                .build();
    }

    public PostsImage storePostsImageThumbnailSave(MultipartFile multipartFile, Long postsId) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFilename)));

        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new PostsNotFound());

        PostsImage postsImage = PostsImage.builder()
                .posts(posts)
                .originalFilename(originalFilename)
                .storeFilename(storeFilename)
                .imageUrl(getFullPath(storeFilename))
                .thumbnailYn(true)
                .build();

        return postsImage;
    }

    public PostsImage storePostsImageThumbnailSaveS3(MultipartFile multipartFile, Long postsId) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);

        if (!isImageExtAllowed(originalFilename)) {
            throw new ImageExtAllowed();
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getInputStream().available());
        objectMetadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, storeFilename, multipartFile.getInputStream(), objectMetadata);

        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new PostsNotFound());

        return PostsImage.builder()
                .posts(posts)
                .originalFilename(originalFilename)
                .storeFilename(storeFilename)
                .imageUrl(amazonS3.getUrl(bucket, storeFilename).toString())
                .thumbnailYn(true)
                .build();
    }

    public MemberImage storeMemberImageThumbnail(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFilename)));

        return MemberImage.builder()
                .originalFilename(originalFilename)
                .storeFilename(storeFilename)
                .imageUrl(amazonS3.getUrl(bucket, storeFilename).toString())
                .thumbnailYn(true)
                .build();
    }

    public MemberImage storeMemberImageThumbnailS3(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);

        if (!isImageExtAllowed(originalFilename)) {
            throw new ImageExtAllowed();
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getInputStream().available());
        objectMetadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, storeFilename, multipartFile.getInputStream(), objectMetadata);

        return MemberImage.builder()
                .originalFilename(originalFilename)
                .storeFilename(storeFilename)
                .imageUrl(amazonS3.getUrl(bucket, storeFilename).toString())
                .thumbnailYn(true)
                .build();
    }

    public MemberImage storeMemberImageThumbnailSave(MultipartFile multipartFile, Long memberId) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFilename)));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFound());

        MemberImage memberImage = MemberImage.builder()
                .member(member)
                .originalFilename(originalFilename)
                .storeFilename(storeFilename)
                .imageUrl(getFullPath(storeFilename))
                .thumbnailYn(true)
                .build();

        return memberImage;
    }

    public MemberImage storeMemberImageThumbnailSaveS3(MultipartFile multipartFile, Long memberId) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);

        if (!isImageExtAllowed(originalFilename)) {
            throw new ImageExtAllowed();
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getInputStream().available());
        objectMetadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, storeFilename, multipartFile.getInputStream(), objectMetadata);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFound());

        return MemberImage.builder()
                .member(member)
                .originalFilename(originalFilename)
                .storeFilename(storeFilename)
                .imageUrl(amazonS3.getUrl(bucket, storeFilename).toString())
                .thumbnailYn(true)
                .build();
    }

    private String createStoreFilename(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
