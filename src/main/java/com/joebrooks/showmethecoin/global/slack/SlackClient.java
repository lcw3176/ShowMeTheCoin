package com.joebrooks.showmethecoin.global.slack;

import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class SlackClient {

    private final String slackUrl = System.getenv("SLACK_URL");

    public void sendMessage() {
        RestTemplate rt = new RestTemplate();
        String message = SlackMessageUtil.makeErrorMessage();
        rt.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        rt.postForObject(slackUrl, message, String.class);
    }
}
