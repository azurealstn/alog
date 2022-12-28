package com.azurealstn.alog.controller.member;

import com.azurealstn.alog.config.auth.CustomOAuth2UserService;
import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.email.EmailAuthRequestDto;
import com.azurealstn.alog.dto.hashtag.HashTagResponseDto;
import com.azurealstn.alog.dto.like.PostsLikeRequestDto;
import com.azurealstn.alog.dto.like.PostsLikeResponseDto;
import com.azurealstn.alog.dto.member.MemberResponseDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.service.comment.CommentService;
import com.azurealstn.alog.service.hashtag.HashTagService;
import com.azurealstn.alog.service.like.PostsLikeService;
import com.azurealstn.alog.service.login.LoginService;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final LoginService loginService;
    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/api/v1/auth/create-member")
    public String createMember(@ModelAttribute EmailAuthRequestDto requestDto, Model model) {
        MemberResponseDto memberResponseDto = memberService.confirmEmailAuth(requestDto);

        model.addAttribute("member", memberResponseDto);

        return "member/create-member";
    }

    @GetMapping("/api/v1/auth/sns-create-member-login")
    public String snsCreateMemberLogin(Model model) {
        Member member = customOAuth2UserService.getSnsMemberInfo();
        boolean existsByEmail = loginService.existsByEmail(member.getEmail());

        model.addAttribute("member", member);

        if (existsByEmail) {
            return "redirect:/api/v1/auth/snsLogin?email=" + member.getEmail();
        } else {
            return "member/create-member";
        }
    }

    @GetMapping("/api/v1/setting/{memberId}")
    public String setting(Model model, @PathVariable Long memberId) {
        MemberResponseDto member = memberService.findById(memberId);

        model.addAttribute("member", member);

        return "member/setting";
    }

    @GetMapping("/api/v1/auth/my-alog/{memberId}")
    public String myAlog(Model model, @PathVariable Long memberId) {
        MemberResponseDto member = memberService.findById(memberId);
        List<PostsResponseDto> postsListByMember = postsService.findAllByMember(memberId);

        postsService.setMemberPostsResponseDto(postsListByMember);

        SessionMemberDto sessionMember = (SessionMemberDto) httpSession.getAttribute("member");
        if (sessionMember != null) {
            Boolean isMyPosts = memberService.isMyPosts(memberId, sessionMember);
            model.addAttribute("isMyPosts", isMyPosts);
            MemberResponseDto sessionMemberDto = memberService.findById(sessionMember.getId());
            model.addAttribute("sessionMemberDto", sessionMemberDto);

        }

        model.addAttribute("member", member);
        model.addAttribute("postsListByMember", postsListByMember);

        return "member/my-alog";
    }

}
