package com.azurealstn.alog.controller.hashtag;

import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.hashtag.HashTagResponseDto;
import com.azurealstn.alog.dto.hashtag.HashTagSearchDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
import com.azurealstn.alog.service.hashtag.HashTagService;
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

    private final HashTagService hashTagService;
    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/api/v1/auth/tags/{name}")
    public String tags(Model model, @PathVariable String name, @ModelAttribute(name = "searchDto") HashTagSearchDto searchDto) {
        SessionMemberDto member = (SessionMemberDto) httpSession.getAttribute("member");
        List<PostsResponseDto> postsList = postsService.findAllJoinWithHashTag(name, searchDto);

        for (PostsResponseDto postsResponseDto : postsList) {
            List<HashTagResponseDto> tags = hashTagService.findByTags(postsResponseDto.getId());
            List<HashTag> hashTags = tags.stream()
                    .map(tag -> HashTag.builder()
                            .name(tag.getName())
                            .build())
                    .collect(Collectors.toList());

            for (HashTag hashTag : hashTags) {
                postsResponseDto.addHashTag(hashTag);
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
        log.info("searchDto={}", searchDto.toString());

        model.addAttribute("member", member);
        model.addAttribute("tagName", name);
        model.addAttribute("totalPostsCountByTag", searchDto.getBasePageDto().getTotalRowCount());
        model.addAttribute("postsList", postsList);
        model.addAttribute("postsSize", postsList.size());

        model.addAttribute("movePrevPage", searchDto.getPage() - 1);
        model.addAttribute("moveNextPage", searchDto.getPage() + 1);
        model.addAttribute("pagination", pagination);
        model.addAttribute("hasDoubleNextPage", hasDoubleNextPage);
        model.addAttribute("doubleNextPage", doubleNextPage);
        model.addAttribute("hasDoublePrevPage", hasDoublePrevPage);
        model.addAttribute("doublePrevPage", doublePrevPage);

        return "hashtag/tags";
    }
}
