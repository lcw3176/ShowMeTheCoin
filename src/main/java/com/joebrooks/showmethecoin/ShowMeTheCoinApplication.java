package com.joebrooks.showmethecoin;

import com.joebrooks.showmethecoin.repository.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.MessagingException;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
@RequiredArgsConstructor
public class ShowMeTheCoinApplication {

    private final UserService userService;

    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        userService.initAllUsersTradeLevel(); // fixme 테스트용도, 차후 제거
    }

    public static void main(String[] args) throws MessagingException {
        SpringApplication.run(ShowMeTheCoinApplication.class, args);
    }

    @PreDestroy
    public void close(){
        userService.stopAllUsersTrade();
    }


}
