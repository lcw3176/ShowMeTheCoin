package com.joebrooks.showmethecoin;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class ShowMeTheCoinApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShowMeTheCoinApplication.class, args);
    }

}
