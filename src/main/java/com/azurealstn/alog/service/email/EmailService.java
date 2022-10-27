package com.azurealstn.alog.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 * 이메일을 전송하는 서비스
 */
@RequiredArgsConstructor
@EnableAsync
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Async
    public void send(String email, String authToken, Boolean existsByEmail) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("alog 회원가입");
        loginOrCreateMemberUrl(msg, existsByEmail, email, authToken);

        javaMailSender.send(msg);
    }

    public void loginOrCreateMemberUrl(SimpleMailMessage msg, Boolean existsByEmail, String email, String authToken) {
        if (existsByEmail) {
            msg.setText("http://localhost:8080/api/v1/auth/email-login?email=" + email + "&authToken=" + authToken);
        } else {
            msg.setText("http://localhost:8080/api/v1/auth/create-member?email=" + email + "&authToken=" + authToken);
        }
    }
}
