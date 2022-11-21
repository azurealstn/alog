package com.azurealstn.alog.service.comment;

import com.azurealstn.alog.Infra.exception.comment.CommentNotFound;
import com.azurealstn.alog.Infra.exception.member.MemberNotFound;
import com.azurealstn.alog.Infra.exception.posts.PostsNotFound;
import com.azurealstn.alog.domain.comment.Comment;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.comment.CommentCreateRequestDto;
import com.azurealstn.alog.dto.comment.CommentModifyRequestDto;
import com.azurealstn.alog.dto.comment.CommentResponseDto;
import com.azurealstn.alog.repository.comment.CommentRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.repository.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostsRepository postsRepository;

    /**
     * 댓글 저장 API
     */
    @Transactional
    public Long create(CommentCreateRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new MemberNotFound());
        Posts posts = postsRepository.findById(requestDto.getPostsId())
                .orElseThrow(() -> new PostsNotFound());

        requestDto.updateMemberAndPosts(member, posts);
        return commentRepository.save(requestDto.toEntity()).getId();
    }

    /**
     * 댓글 수정 API
     */
    @Transactional
    public Long modify(Long commentId, CommentModifyRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFound());

        comment.modifyContent(requestDto.getContent());

        return commentId;
    }

    /**
     * 댓굴 삭제 API
     */
    @Transactional
    public void delete(Long commentId) {
        List<Comment> commentLevel1 = commentRepository.findAllCommentLevel1(commentId);

        if (!CollectionUtils.isEmpty(commentLevel1)) {
            for (Comment subComment : commentLevel1) {
                commentRepository.deleteById(subComment.getId());
            }
        }

        commentRepository.deleteById(commentId);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAllCommentLevel0() {
        List<Comment> commentList = commentRepository.findAllCommentLevel0();
        return commentList.stream()
                .map(comment -> new CommentResponseDto(comment))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAllCommentLevel1(Long commentId) {
        List<Comment> commentList = commentRepository.findAllCommentLevel1(commentId);
        return commentList.stream()
                .map(comment -> new CommentResponseDto(comment))
                .collect(Collectors.toList());
    }
}
