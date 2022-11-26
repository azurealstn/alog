package com.azurealstn.alog.Infra.utils;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class FileUtils {

    private final PostsRepository postsRepository;
    private final MemberRepository memberRepository;

    @Value("${upload.path}")
    private String uploadPath;

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

    public MemberImage storeMemberImageThumbnail(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFilename)));

        MemberImage memberImage = MemberImage.builder()
                .originalFilename(originalFilename)
                .storeFilename(storeFilename)
                .imageUrl(getFullPath(storeFilename))
                .thumbnailYn(true)
                .build();

        return memberImage;
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
