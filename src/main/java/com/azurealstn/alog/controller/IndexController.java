package com.azurealstn.alog.controller;

import com.azurealstn.alog.dto.PaginationDto;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
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

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final HttpSession httpSession;
    private final PostsService postsService;
    private final MemberService memberService;

    @GetMapping("/")
    public String index(Model model, @ModelAttribute(name = "searchDto") PostsSearchDto searchDto) {
        SessionMemberDto sessionMemberDto = (SessionMemberDto) httpSession.getAttribute("member");
        if (sessionMemberDto != null) {
            MemberResponseDto member = memberService.findById(sessionMemberDto.getId());
            model.addAttribute("member", member);
        }

        List<PostsResponseDto> postsList = postsService.findAll(searchDto);

        postsService.setIndexPostsResponseDto(postsList);

        PaginationDto paginationDto = new PaginationDto(searchDto.getBasePageDto().getStartPage(), searchDto.getBasePageDto().getEndPage(), searchDto.getPage(), searchDto.getBasePageDto().getTotalPageCount());

        model.addAttribute("postsList", postsList);
        model.addAttribute("movePrevPage", paginationDto.getMovePrevPage());
        model.addAttribute("moveNextPage", paginationDto.getMoveNextPage());
        model.addAttribute("pagination", paginationDto.getPagination());
        model.addAttribute("hasDoubleNextPage", paginationDto.isHasDoubleNextPage());
        model.addAttribute("doubleNextPage", paginationDto.getDoubleNextPage());
        model.addAttribute("hasDoublePrevPage", paginationDto.isHasDoublePrevPage());
        model.addAttribute("doublePrevPage", paginationDto.getDoublePrevPage());

        return "index";
    }

    @GetMapping("/api/v1/auth/popular")
    public String liked(Model model, @ModelAttribute(name = "searchDto") PostsSearchDto searchDto) {
        SessionMemberDto sessionMemberDto = (SessionMemberDto) httpSession.getAttribute("member");
        if (sessionMemberDto != null) {
            MemberResponseDto member = memberService.findById(sessionMemberDto.getId());
            model.addAttribute("member", member);
        }

        List<PostsResponseDto> postsList = postsService.findAllByIndexLiked(searchDto);

        postsService.setIndexPostsResponseDto(postsList);

        PaginationDto paginationDto = new PaginationDto(searchDto.getBasePageDto().getStartPage(), searchDto.getBasePageDto().getEndPage(), searchDto.getPage(), searchDto.getBasePageDto().getTotalPageCount());

        model.addAttribute("postsList", postsList);
        model.addAttribute("movePrevPage", paginationDto.getMovePrevPage());
        model.addAttribute("moveNextPage", paginationDto.getMoveNextPage());
        model.addAttribute("pagination", paginationDto.getPagination());
        model.addAttribute("hasDoubleNextPage", paginationDto.isHasDoubleNextPage());
        model.addAttribute("doubleNextPage", paginationDto.getDoubleNextPage());
        model.addAttribute("hasDoublePrevPage", paginationDto.isHasDoublePrevPage());
        model.addAttribute("doublePrevPage", paginationDto.getDoublePrevPage());

        return "popular";
    }
}
