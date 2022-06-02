package com.joebrooks.showmethecoin;

import com.joebrooks.showmethecoin.repository.user.UserService;
import com.joebrooks.showmethecoin.repository.userConfig.UserConfigService;
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

    private final UserConfigService userConfigService;

    public static void main(String[] args) throws MessagingException {
        SpringApplication.run(ShowMeTheCoinApplication.class, args);
    }

    @PreDestroy
    public void close(){
        userConfigService.stopAllUsersTrade();
    }


}
