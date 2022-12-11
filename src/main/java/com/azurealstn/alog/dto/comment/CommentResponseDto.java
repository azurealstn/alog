package com.azurealstn.alog.dto.comment;

import com.azurealstn.alog.Infra.utils.DateUtils;
import com.azurealstn.alog.domain.comment.Comment;
import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class CommentResponseDto implements Serializable {
    private final Long id;
    private final String content;
    private final Member member;
    private final Posts posts;
    private final Long upCommentId;
    private final int level;
    private final String previousTime;
    private List<Comment> subCommentList;
    private boolean isCommentMe;
    private boolean hasSubCommentList;
    private int subCommentListCount;

    public CommentResponseDto(Comment entity) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.member = entity.getMember();
        this.posts = entity.getPosts();
        this.upCommentId = entity.getUpCommentId();
        this.level = entity.getLevel();
        this.previousTime = DateUtils.previousTimeCalc(entity.getCreatedDate());
    }

    public void addSubCommentList(List<Comment> subCommentList) {
        this.subCommentList = subCommentList;
    }

    public void addIsCommentMe(boolean isCommentMe) {
        this.isCommentMe = isCommentMe;
    }

    public void addHasSubCommentList(boolean hasSubCommentList) {
        this.hasSubCommentList = hasSubCommentList;
    }

    public void addSubCommentListCount(int subCommentListCount) {
        this.subCommentListCount = subCommentListCount;
    }

}
