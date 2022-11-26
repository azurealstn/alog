package com.azurealstn.alog.repository.image;

import com.azurealstn.alog.domain.image.MemberImage;
import com.azurealstn.alog.domain.image.PostsImage;

import java.util.Optional;

public interface MemberImageRepositoryCustom {

    Optional<MemberImage> findThumbnailByMember(Long memberId);

    void deleteByThumbnail(Long memberId);
}
