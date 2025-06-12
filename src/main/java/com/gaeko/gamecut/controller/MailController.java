package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.util.GamecutUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.internet.MimeMessage;

@Controller
@Slf4j
public class MailController {
    @Autowired
    private MailSender mailSender;
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${send.mail.address}")
    private String sendEmail;

    @GetMapping("/sendHTML")
    @ResponseBody
    public String sendHTML(HttpServletRequest request) {
        javaMailSender.send(new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                String text = "";
                text += "<html>";
                text += "<h2> 내가 살면서 인생에서 중요하게 여기는 것 </h2>";
                text += "<ul>";
                text += "<li> 전쟁 </li>";
                text += "<img src ='cid:myDog'/>";
                text += "</ul>";
                text += "</html>";
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setSubject("html 이메일 보내기 연습");
                mimeMessageHelper.setFrom("");
                mimeMessageHelper.setTo("");
                mimeMessageHelper.setText(text, true);
                mimeMessageHelper.addInline("myDog", new ClassPathResource("dog2.png"));
                mimeMessageHelper.addAttachment("hello.txt", new ClassPathResource("hello.txt"));
            }
        });
        return "OK";
    }

    @GetMapping("/sendMail")
    @ResponseBody
    public String send(HttpServletRequest request, String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sendEmail);
        mailMessage.setTo(email);
        mailMessage.setSubject("나니가 스키?");
        //mailMessage.setText("자바보다 파이썬");

        GamecutUtil util = new GamecutUtil();
        String data = util.getCode(6);
        mailMessage.setText(data);

        try {
            mailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println("예외발생 : " + e.getMessage());
        }

        return "OK";
    }
}
