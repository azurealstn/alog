package com.azurealstn.alog.repository.comment;

import com.azurealstn.alog.domain.comment.Comment;
import com.azurealstn.alog.domain.email.EmailAuth;
import com.azurealstn.alog.repository.email.EmailAuthRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
