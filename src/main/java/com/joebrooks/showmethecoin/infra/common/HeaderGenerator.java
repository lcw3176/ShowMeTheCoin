package com.joebrooks.showmethecoin.infra.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HeaderGenerator {

    public static HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", TokenGenerator.getAuthToken());

        return headers;
    }



    public static HttpHeaders getHeaders(String queryHash){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", TokenGenerator.getAuthToken(queryHash));

        return headers;
    }

}
