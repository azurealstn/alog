package com.azurealstn.alog.service.email;

import com.azurealstn.alog.domain.email.EmailAuth;
import com.azurealstn.alog.dto.login.LoginRequestDto;
import com.azurealstn.alog.dto.member.MemberCreateRequestDto;
import com.azurealstn.alog.repository.email.EmailAuthRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.service.login.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EmailService emailService;

    @BeforeEach
    void beforeEach() throws Exception {
        emailAuthRepository.deleteAll();
        memberRepository.deleteAll();
    }

}