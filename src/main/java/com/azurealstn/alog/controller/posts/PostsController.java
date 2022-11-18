package com.azurealstn.alog.controller.posts;

import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.dto.tempsave.TempSaveResponseDto;
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

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostsController {

    private final PostsService postsService;
    private final TempSaveService tempSaveService;
    private final HttpSession httpSession;

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

        model.addAttribute("member", member);
        model.addAttribute("posts", posts);
        model.addAttribute("previousPostsDate", previousPostsDate);
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("isSecret", posts.getSecret());

        return "posts/detailed-posts";
    }

    @GetMapping("/api/v1/write/{postsId}")
    public String update_write(@PathVariable Long postsId, Model model) {
        PostsResponseDto posts = postsService.findById(postsId);

        model.addAttribute("posts", posts);

        return "posts/modify-posts";
    }
}
