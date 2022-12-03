package com.azurealstn.alog.controller.search;

import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.member.MemberResponseDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
import com.azurealstn.alog.service.member.MemberService;
import com.azurealstn.alog.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SearchController {

    private final MemberService memberService;
    private final HttpSession httpSession;

    @GetMapping("/api/v1/auth/search")
    public String search(Model model,
                         @RequestParam(required = false) Integer page,
                         @RequestParam(required = false) String searchValue) {
        SessionMemberDto sessionMemberDto = (SessionMemberDto) httpSession.getAttribute("member");
        MemberResponseDto member = memberService.findById(sessionMemberDto.getId());

        model.addAttribute("member", member);
        model.addAttribute("searchValue", searchValue);

        return "search/search";
    }
}
