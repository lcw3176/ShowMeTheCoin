package com.joebrooks.showmethecoin.global.routine.mail;

import com.joebrooks.showmethecoin.repository.dailyScore.DailyScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class ReportSender {

//    @Value("${spring.mail.username}")
//    private String sender;
//
//    @Value("${spring.mail.receiver")
//    private String receiver;
//    private final JavaMailSender mailSender;
//
//
//    public void sendReport(double benefit, double balance) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message,"UTF-8");
//        helper.setTo(receiver);
//        helper.setFrom(sender);
//        helper.setSubject(LocalDate.now(ZoneId.of("Asia/Seoul")) + " 수익 보고서");
//        helper.setText("수익: " + benefit + " \n잔고: " + balance);
//        mailSender.send(message);
//    }

}
