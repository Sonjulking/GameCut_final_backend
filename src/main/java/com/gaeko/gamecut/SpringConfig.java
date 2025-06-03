package com.gaeko.gamecut;

import kr.co.youiwe.webservice.BitSms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class SpringConfig {
    @Bean
    public BitSms sms() {
        return new BitSms();
    }

    @Value("${send.mail.address}")
    private String mailAddress;
    @Value("${send.mail.password}")
    private String mailPassword;

    @Bean
    public JavaMailSenderImpl javaMailSender() {
        JavaMailSenderImpl r = new JavaMailSenderImpl();
        r.setHost("smtp.gmail.com");
        r.setPort(587);
        r.setUsername(mailAddress);    // gmail계정을 설정합니다.
        r.setPassword(mailPassword);  // <--- 발행된 암호를 여기에 넣어 주세요.
        r.setDefaultEncoding("UTF-8");

        Properties prop = new Properties();
        prop.put("mail.smtp.starttls.enable", true);
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.ssl.checkserveridentity", true);
        prop.put("mail.smtp.ssl.trust", "*");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        r.setJavaMailProperties(prop);
        return r;
    }

}