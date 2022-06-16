package com.joebrooks.showmethecoin;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.mail.MessagingException;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
@RequiredArgsConstructor
public class ShowMeTheCoinApplication {

    public static void main(String[] args) throws MessagingException {
        SpringApplication.run(ShowMeTheCoinApplication.class, args);
    }

}
