package com.joebrooks.showmethecoin.service;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Service
public class SlackService {

    private String url = System.getenv("slack_url");

    public void sendMessage(String message){
        RestTemplate rt = new RestTemplate();

        rt.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        rt.postForObject(url, message, String.class);
    }
}