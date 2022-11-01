package com.azurealstn.alog.controller.posts;

import com.azurealstn.alog.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostsController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/api/v1/write")
    public String write(HttpServletRequest request, Model model) {
        postsService.savePrevPage(request);
        String prevUrl = (String) httpSession.getAttribute("prevUrl");
        model.addAttribute("prevUrl", prevUrl);
        return "/posts/create-posts";
    }
}
