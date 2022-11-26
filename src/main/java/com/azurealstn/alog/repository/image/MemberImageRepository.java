package com.azurealstn.alog.repository.image;

import com.azurealstn.alog.domain.image.MemberImage;
import com.azurealstn.alog.domain.image.PostsImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long>, MemberImageRepositoryCustom {
}
