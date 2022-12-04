package com.azurealstn.alog.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 이메일을 전송하는 서비스
 */
@RequiredArgsConstructor
@EnableAsync
@Service
public class EmailService {

    @Value("${domain.location}")
    private String domainName;

    private final JavaMailSender javaMailSender;

    @Async
    public void send(String email, String authToken, Boolean existsByEmail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        messageHelper.setTo(email);
        loginOrCreateMemberUrl(messageHelper, existsByEmail, email, authToken);

        javaMailSender.send(message);
    }

    public void loginOrCreateMemberUrl(MimeMessageHelper messageHelper, Boolean existsByEmail, String email, String authToken) throws MessagingException {
        if (existsByEmail) {
            String loginText = createMemberLoginHtml(email, authToken);
            messageHelper.setSubject("alog 로그인");
            messageHelper.setText(loginText, true);
        } else {
            String createText = createMemberAddHtml(email, authToken);
            messageHelper.setSubject("alog 회원가입");
            messageHelper.setText(createText, true);
        }
    }

    private String createMemberLoginHtml(String email, String authToken) {
        StringBuilder html = new StringBuilder();
        html.append("<a style='text-decoration: none; color: #1e1e23;' href='").append(domainName).append("' rel='noreferrer noopener' target='_blank'>");
        html.append("   <h1 style='display: flex; justify-content: center; margin: 1rem 0;'>alog</h1>");
        html.append("</a>");
        html.append("<div style='max-width: 100%; width: 400px; margin: 0 auto; padding: 1rem; text-align: justify; background: #f8f9fa; border: 1px solid #dee2e6; box-sizing: border-box; border-radius: 4px; color: #868e96; margin-top: 0.5rem; box-sizing: border-box;'>");
        html.append("   <b style='black'>안녕하세요!</b>");
        html.append("   로그인을 계속하시려면 하단의 버튼을 클릭하세요. 만약에 실수로 요청하셨거나, 본인이 요청하지 않았다면, 이 메일을 무시하세요.");
        html.append("</div>");
        html.append("<a style='text-decoration: none; : 400px; text-align:center; display:block; margin: 0 auto; margin-top: 1rem; background: #db8056; padding-top: 1rem; color: white; font-size: 1.25rem; padding-bottom: 1rem; font-weight: 600; border-radius: 4px;' href='").append(domainName).append("api/v1/auth/email-login?email=").append(email).append("&authToken=").append(authToken).append("' rel='noreferrer noopener' target='_blank'>계속하기</a>");
        html.append("<div style='text-align: center; margin-top: 1rem; color: #868e96; font-size: 0.85rem;'>");
        html.append("   <div>위 버튼을 클릭해주세요.");
        html.append("       <br>");
        html.append("   </div>");
        html.append("   <br>");
        html.append("   <div>이 링크는 1회성입니다. 한번 클릭하시면 유효성이 사라집니다.</div>");
        html.append("</div>");

        return html.toString();
    }

    private String createMemberAddHtml(String email, String authToken) {
        StringBuilder html = new StringBuilder();
        html.append("<a style='text-decoration: none; color: #1e1e23;' href='").append(domainName).append("' rel='noreferrer noopener' target='_blank'>");
        html.append("   <h1 style='display: flex; justify-content: center; margin: 1rem 0;'>alog</h1>");
        html.append("</a>");
        html.append("<div style='max-width: 100%; width: 400px; margin: 0 auto; padding: 1rem; text-align: justify; background: #f8f9fa; border: 1px solid #dee2e6; box-sizing: border-box; border-radius: 4px; color: #868e96; margin-top: 0.5rem; box-sizing: border-box;'>");
        html.append("   <b style='black'>안녕하세요!</b>");
        html.append("   회원가입을 계속하시려면 하단의 버튼을 클릭하세요. 만약에 실수로 요청하셨거나, 본인이 요청하지 않았다면, 이 메일을 무시하세요.");
        html.append("</div>");
        html.append("<a style='text-decoration: none; : 400px; text-align:center; display:block; margin: 0 auto; margin-top: 1rem; background: #db8056; padding-top: 1rem; color: white; font-size: 1.25rem; padding-bottom: 1rem; font-weight: 600; border-radius: 4px;' href='").append(domainName).append("api/v1/auth/create-member?email=").append(email).append("&authToken=").append(authToken).append("' rel='noreferrer noopener' target='_blank'>계속하기</a>");
        html.append("<div style='text-align: center; margin-top: 1rem; color: #868e96; font-size: 0.85rem;'>");
        html.append("   <div>위 버튼을 클릭해주세요.");
        html.append("       <br>");
        html.append("   </div>");
        html.append("   <br>");
        html.append("   <div>이 링크는 1회성입니다. 한번 클릭하시면 유효성이 사라집니다.</div>");
        html.append("</div>");

        return html.toString();
    }
}
