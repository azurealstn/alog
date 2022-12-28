package com.azurealstn.alog.controller.hashtag;

import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.dto.PaginationDto;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.hashtag.HashTagResponseDto;
import com.azurealstn.alog.dto.hashtag.HashTagSearchDto;
import com.azurealstn.alog.dto.image.PostsImageResponseDto;
import com.azurealstn.alog.dto.like.PostsLikeRequestDto;
import com.azurealstn.alog.dto.like.PostsLikeResponseDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
import com.azurealstn.alog.service.comment.CommentService;
import com.azurealstn.alog.service.hashtag.HashTagService;
import com.azurealstn.alog.service.image.PostsImageService;
import com.azurealstn.alog.service.like.PostsLikeService;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HashTagController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/api/v1/auth/tags/{name}")
    public String tags(Model model, @PathVariable String name, @ModelAttribute(name = "searchDto") HashTagSearchDto searchDto) {
        SessionMemberDto member = (SessionMemberDto) httpSession.getAttribute("member");
        List<PostsResponseDto> postsList = postsService.findAllJoinWithHashTag(name, searchDto);

        postsService.setTagPostsResponseDto(postsList);

        PaginationDto paginationDto = new PaginationDto(searchDto.getBasePageDto().getStartPage(), searchDto.getBasePageDto().getEndPage(), searchDto.getPage(), searchDto.getBasePageDto().getTotalPageCount());

        model.addAttribute("member", member);
        model.addAttribute("tagName", name);
        model.addAttribute("totalPostsCountByTag", searchDto.getBasePageDto().getTotalRowCount());
        model.addAttribute("postsList", postsList);
        model.addAttribute("postsSize", postsList.size());

        model.addAttribute("movePrevPage", paginationDto.getMovePrevPage());
        model.addAttribute("moveNextPage", paginationDto.getMoveNextPage());
        model.addAttribute("pagination", paginationDto.getPagination());
        model.addAttribute("hasDoubleNextPage", paginationDto.isHasDoubleNextPage());
        model.addAttribute("doubleNextPage", paginationDto.getDoubleNextPage());
        model.addAttribute("hasDoublePrevPage", paginationDto.isHasDoublePrevPage());
        model.addAttribute("doublePrevPage", paginationDto.getDoublePrevPage());

        return "hashtag/tags";
    }
}
