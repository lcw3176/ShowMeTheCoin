package com.joebrooks.showmethecoin.global.routine.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

//    private final String email = System.getenv("smtpEmail");
//    private final String password = System.getenv("smtpPassword");
//
//    @Bean
//    public JavaMailSender getJavaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.naver.com");
//        mailSender.setPort(465);
//
//        mailSender.setUsername(email);
//        mailSender.setPassword(password);
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.ssl.enable", "true");
//        props.put("mail.smtp.ssl.trust", "smtp.naver.com");
//        props.put("mail.smtp.starttls.enable", "true");
//
//        return mailSender;
//    }
}
