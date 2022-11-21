package com.azurealstn.alog.controller.posts;

import com.azurealstn.alog.domain.comment.Comment;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.comment.CommentResponseDto;
import com.azurealstn.alog.dto.hashtag.HashTagResponseDto;
import com.azurealstn.alog.dto.like.PostsLikeRequestDto;
import com.azurealstn.alog.dto.like.PostsLikeResponseDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.dto.tempsave.TempSaveResponseDto;
import com.azurealstn.alog.service.comment.CommentService;
import com.azurealstn.alog.service.hashtag.HashTagService;
import com.azurealstn.alog.service.like.PostsLikeService;
import com.azurealstn.alog.service.posts.PostsService;
import com.azurealstn.alog.service.tempsave.TempSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostsController {

    private final PostsService postsService;
    private final TempSaveService tempSaveService;
    private final HttpSession httpSession;
    private final HashTagService hashTagService;
    private final PostsLikeService postsLikeService;
    private final CommentService commentService;

    @GetMapping("/api/v1/write")
    public String write(HttpServletRequest request, Model model, @RequestParam(required = false) String tempCode) {
        postsService.savePrevPage(request);
        String prevUrl = (String) httpSession.getAttribute("prevUrl");

        TempSaveResponseDto tempSave = null;
        if (tempCode != null) {
            tempSave = tempSaveService.findByTempCode(tempCode);
        }

        model.addAttribute("prevUrl", prevUrl);
        model.addAttribute("tempSave", tempSave);

        return "/posts/create-posts";
    }

    @GetMapping("/api/v1/auth/posts/{postsId}")
    public String detail_posts(@PathVariable Long postsId, Model model) {
        PostsResponseDto posts = postsService.findById(postsId);
        String previousPostsDate = postsService.previousPostsDate(postsId);
        SessionMemberDto member = (SessionMemberDto) httpSession.getAttribute("member");
        Boolean isAuthenticated = postsService.isAuthenticated(member, posts);
        List<HashTagResponseDto> hashTagList = hashTagService.findByTags(postsId);

        PostsLikeRequestDto postsLikeRequestDto = new PostsLikeRequestDto(posts.getMember().getId(), postsId);
        PostsLikeResponseDto postsLikeInfo = postsLikeService.findPostsLikeInfo(postsLikeRequestDto);

        List<CommentResponseDto> commentLevel0 = commentService.findAllCommentLevel0();

        for (CommentResponseDto commentResponseDto : commentLevel0) {
            List<CommentResponseDto> commentResponseDtoList = commentService.findAllCommentLevel1(commentResponseDto.getId());
            List<Comment> commentList = commentResponseDtoList.stream()
                    .map(comment -> Comment.builder()
                            .id(comment.getId())
                            .content(comment.getContent())
                            .member(comment.getMember())
                            .posts(comment.getPosts())
                            .level(comment.getLevel())
                            .upCommentId(comment.getUpCommentId())
                            .isCommentMe(comment.getMember().getId().equals((member != null) ? member.getId() : 0))
                            .build())
                    .collect(Collectors.toList());

            commentResponseDto.addSubCommentList(commentList);

            if (commentList.size() > 0) {
                commentResponseDto.addHasSubCommentList(true);
            } else {
                commentResponseDto.addHasSubCommentList(false);
            }

            if (member != null) {
                if (commentResponseDto.getMember().getId().equals(member.getId())) {
                    commentResponseDto.addIsCommentMe(true);
                } else {
                    commentResponseDto.addIsCommentMe(false);
                }
            }
        }


        model.addAttribute("member", member);
        model.addAttribute("posts", posts);
        model.addAttribute("previousPostsDate", previousPostsDate);
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("isSecret", posts.getSecret());
        model.addAttribute("hashTagList", hashTagList);
        model.addAttribute("existHeart", postsLikeInfo.isExist());
        model.addAttribute("likeCount", postsLikeInfo.getPostsLikeCount());
        model.addAttribute("commentLevel0", commentLevel0);

        return "posts/detailed-posts";
    }

    @GetMapping("/api/v1/write/{postsId}")
    public String update_write(@PathVariable Long postsId, Model model) {
        PostsResponseDto posts = postsService.findById(postsId);
        List<HashTagResponseDto> hashTagList = hashTagService.findByTags(postsId);

        model.addAttribute("posts", posts);

        return "posts/modify-posts";
    }
}
