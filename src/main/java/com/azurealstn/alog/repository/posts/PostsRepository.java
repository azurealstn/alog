package com.azurealstn.alog.repository.posts;

import com.azurealstn.alog.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {
}
