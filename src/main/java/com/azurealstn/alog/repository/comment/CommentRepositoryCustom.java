package com.azurealstn.alog.repository.comment;

import com.azurealstn.alog.domain.comment.Comment;
import com.azurealstn.alog.domain.email.EmailAuth;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepositoryCustom {

    List<Comment> findAllCommentLevel0();

    List<Comment> findAllCommentLevel1(Long commentId);

    int commentCountByPosts(Long postsId);

}
