package com.azurealstn.alog.controller;

import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {
        SessionMemberDto member = (SessionMemberDto) httpSession.getAttribute("member");

        if (member != null) {
            model.addAttribute("member", member);
        }

        return "index";
    }
}
