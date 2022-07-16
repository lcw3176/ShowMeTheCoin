package com.joebrooks.showmethecoin;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
@RequiredArgsConstructor
public class ShowMeTheCoinApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShowMeTheCoinApplication.class, args);
    }

}
