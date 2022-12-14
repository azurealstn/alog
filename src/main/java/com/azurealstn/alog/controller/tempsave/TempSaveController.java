package com.azurealstn.alog.controller.tempsave;

import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.member.MemberResponseDto;
import com.azurealstn.alog.dto.tempsave.TempSaveResponseDto;
import com.azurealstn.alog.service.member.MemberService;
import com.azurealstn.alog.service.tempsave.TempSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class TempSaveController {

    private final TempSaveService tempSaveService;
    private final MemberService memberService;
    private final HttpSession httpSession;

    @GetMapping("/api/v1/temp-saves")
    public String findAll(Model model) {
        SessionMemberDto sessionMemberDto = (SessionMemberDto) httpSession.getAttribute("member");
        MemberResponseDto member = memberService.findById(sessionMemberDto.getId());
        List<TempSaveResponseDto> tempSaveList = tempSaveService.findAll(member.getId());

        model.addAttribute("tempSaveList", tempSaveList);
        model.addAttribute("member", member);

        return "tempsave/temp-saves";
    }
}
