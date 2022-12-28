package com.azurealstn.alog.controller.like;

import com.azurealstn.alog.dto.PaginationDto;
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
    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/api/v1/liked/{memberId}")
    public String liked(Model model, @PathVariable Long memberId, @ModelAttribute(name = "searchDto") PostsSearchDto searchDto) {
        SessionMemberDto sessionMember = (SessionMemberDto) httpSession.getAttribute("member");
        MemberResponseDto sessionMemberDto = memberService.findById(sessionMember.getId());

        List<PostsResponseDto> postsList = postsService.findAllByLike(searchDto, memberId);

        postsService.setPostsLikeResponseDto(postsList);

        PaginationDto paginationDto = new PaginationDto(searchDto.getBasePageDto().getStartPage(), searchDto.getBasePageDto().getEndPage(), searchDto.getPage(), searchDto.getBasePageDto().getTotalPageCount());

        model.addAttribute("postsList", postsList);
        model.addAttribute("hasPostsList", postsList.size() > 0);
        model.addAttribute("movePrevPage", paginationDto.getMovePrevPage());
        model.addAttribute("moveNextPage", paginationDto.getMoveNextPage());
        model.addAttribute("pagination", paginationDto.getPagination());
        model.addAttribute("hasDoubleNextPage", paginationDto.isHasDoubleNextPage());
        model.addAttribute("doubleNextPage", paginationDto.getDoubleNextPage());
        model.addAttribute("hasDoublePrevPage", paginationDto.isHasDoublePrevPage());
        model.addAttribute("doublePrevPage", paginationDto.getDoublePrevPage());

        model.addAttribute("sessionMemberDto", sessionMemberDto);

        return "like/liked";
    }
}
