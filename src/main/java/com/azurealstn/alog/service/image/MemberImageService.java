package com.azurealstn.alog.service.image;

import com.azurealstn.alog.Infra.utils.FileUtils;
import com.azurealstn.alog.domain.image.MemberImage;
import com.azurealstn.alog.domain.image.PostsImage;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.dto.image.MemberImageResponseDto;
import com.azurealstn.alog.dto.image.PostsImageResponseDto;
import com.azurealstn.alog.repository.image.MemberImageRepository;
import com.azurealstn.alog.repository.image.PostsImageRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberImageService {

    private final FileUtils fileUtils;
    private final MemberImageRepository memberImageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Resource displayImage(String storeFilename) throws Exception {
        return new UrlResource("file:" + fileUtils.getFullPath(storeFilename));
    }

    @Transactional
    public MemberImageResponseDto thumbnailUpload(MultipartFile multipartFile) throws IOException {
        MemberImage memberImage = fileUtils.storeMemberImageThumbnail(multipartFile);
        return new MemberImageResponseDto(memberImage);
    }

    @Transactional
    public MemberImageResponseDto thumbnailUploadSave(MultipartFile multipartFile, Long memberId) throws IOException {
        MemberImage memberImage = fileUtils.storeMemberImageThumbnailSave(multipartFile, memberId);
        Optional<MemberImage> existsMemberImage = memberImageRepository.findThumbnailByMember(memberId);
        existsMemberImage.ifPresent(memberImageRepository::delete);
        memberImageRepository.save(memberImage);

        return new MemberImageResponseDto(memberImage);
    }

    @Transactional(readOnly = true)
    public MemberImageResponseDto findThumbnailByPosts(Long memberId) {
        Optional<MemberImage> memberImage = memberImageRepository.findThumbnailByMember(memberId);
        if (memberImage.isPresent()) {
            return new MemberImageResponseDto(memberImage.get());
        } else {
            return null;
        }
    }

    @Transactional
    public void deleteByThumbnail(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isPresent()) {
            memberImageRepository.deleteByThumbnail(memberId);
        }
    }
}
