package com.azurealstn.alog.controller;

import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
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

    @GetMapping("/")
    public String index(Model model, @ModelAttribute(name = "searchDto") PostsSearchDto searchDto) {
        SessionMemberDto member = (SessionMemberDto) httpSession.getAttribute("member");
        if (member != null) {
            model.addAttribute("member", member);
        }

        List<PostsResponseDto> postsList = postsService.findAll(searchDto);

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
        model.addAttribute("movePrevPage", searchDto.getPage() - 1);
        model.addAttribute("moveNextPage", searchDto.getPage() + 1);
        model.addAttribute("pagination", pagination);
        model.addAttribute("hasDoubleNextPage", hasDoubleNextPage);
        model.addAttribute("doubleNextPage", doubleNextPage);
        model.addAttribute("hasDoublePrevPage", hasDoublePrevPage);
        model.addAttribute("doublePrevPage", doublePrevPage);

        return "index";
    }
}
