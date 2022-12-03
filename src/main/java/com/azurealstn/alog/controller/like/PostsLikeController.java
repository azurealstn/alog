package com.azurealstn.alog.controller.like;

import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.image.PostsImageResponseDto;
import com.azurealstn.alog.dto.like.PostsLikeRequestDto;
import com.azurealstn.alog.dto.like.PostsLikeResponseDto;
import com.azurealstn.alog.dto.member.MemberResponseDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
import com.azurealstn.alog.service.comment.CommentService;
import com.azurealstn.alog.service.image.PostsImageService;
import com.azurealstn.alog.service.like.PostsLikeService;
import com.azurealstn.alog.service.member.MemberService;
import com.azurealstn.alog.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostsLikeController {

    private final MemberService memberService;
    private final PostsLikeService postsLikeService;
    private final CommentService commentService;
    private final PostsService postsService;
    private final PostsImageService postsImageService;
    private final HttpSession httpSession;

    @GetMapping("/api/v1/liked/{memberId}")
    public String liked(Model model, @PathVariable Long memberId, @ModelAttribute(name = "searchDto") PostsSearchDto searchDto) {
        SessionMemberDto sessionMember = (SessionMemberDto) httpSession.getAttribute("member");
        MemberResponseDto sessionMemberDto = memberService.findById(sessionMember.getId());

        List<PostsResponseDto> postsList = postsService.findAllByLike(searchDto, memberId);

        for (PostsResponseDto postsResponseDto : postsList) {
            PostsLikeRequestDto postsLikeRequestDto = new PostsLikeRequestDto(postsResponseDto.getMember().getId(), postsResponseDto.getId());
            PostsLikeResponseDto postsLikeInfo = postsLikeService.findPostsLikeInfo(postsLikeRequestDto);
            postsResponseDto.addLikeCount(postsLikeInfo.getPostsLikeCount());
            postsResponseDto.addCommentCount(commentService.commentCountByPosts(postsResponseDto.getId()));

            PostsImageResponseDto postsImageResponseDto = postsImageService.findThumbnailByPosts(postsResponseDto.getId());
            if (postsImageResponseDto != null) {
                postsResponseDto.addStoreFilename(postsImageResponseDto.getStoreFilename());
                postsResponseDto.addImageUrl(postsImageResponseDto.getImageUrl());
            }
        }

        //==페이징 처리 start==//
        List<Integer> pagination = new ArrayList<>();
        int startPage = searchDto.getBasePageDto().getStartPage();
        int endPage = searchDto.getBasePageDto().getEndPage();
        for (int i = startPage; i <= endPage; i++) {
            pagination.add(i);
        }

        boolean hasDoublePrevPage = (searchDto.getPage() / 10) > 0;
        boolean hasDoubleNextPage = (searchDto.getPage() / 10) < (searchDto.getBasePageDto().getTotalPageCount() / 10);
        int doublePrevPage = startPage - 10;
        int doubleNextPage = startPage + 10;

        //==페이징 처리 end==//

        model.addAttribute("postsList", postsList);
        model.addAttribute("hasPostsList", postsList.size() > 0);
        model.addAttribute("movePrevPage", searchDto.getPage() - 1);
        model.addAttribute("moveNextPage", searchDto.getPage() + 1);
        model.addAttribute("pagination", pagination);
        model.addAttribute("hasDoubleNextPage", hasDoubleNextPage);
        model.addAttribute("doubleNextPage", doubleNextPage);
        model.addAttribute("hasDoublePrevPage", hasDoublePrevPage);
        model.addAttribute("doublePrevPage", doublePrevPage);

        model.addAttribute("sessionMemberDto", sessionMemberDto);

        return "like/liked";
    }
}
