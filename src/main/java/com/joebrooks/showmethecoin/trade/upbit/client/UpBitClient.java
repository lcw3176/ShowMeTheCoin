package com.joebrooks.showmethecoin.trade.upbit.client;

import com.google.gson.Gson;
import com.joebrooks.showmethecoin.global.httpClient.ClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class UpBitClient {

    private final String baseUrl = "https://api.upbit.com/v1";
    private final String accessKey = System.getenv("accessKey");
    private final String secretKey = System.getenv("secretKey");

    private final WebClient webClient;
    private final int timeoutMillis = 3000;


    public <T> T[] get(String path, boolean authHeaderRequired, Class<T[]> clazz){
        try {
            return webClient.get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(headers -> {
                        if(authHeaderRequired){
                            headers.add("Authorization",
                                    HeaderGenerator.getJwtHeader(accessKey, secretKey));
                        }
                    })
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> Mono.error(new IllegalStateException("failed get")))
                    .bodyToMono(clazz)
                    .block();

        } catch (Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <T> T[] get(String path, boolean authHeaderRequired, Object queryParams, Class<T[]> clazz){
        HashMap<String, String> map = QueryGenerator.getQueryMap(queryParams);

        try {
            return webClient.get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(headers -> {
                        if(authHeaderRequired){
                            try {
                                headers.add("Authorization",
                                        HeaderGenerator.getJwtHeader(accessKey, secretKey, QueryGenerator.convertQueryToHash(map)));
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> Mono.error(new IllegalStateException("failed get")))
                    .bodyToMono(clazz)
                    .block();

        } catch (Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <T> T post(String path, Object body, Class<T> clazz){
        HashMap<String, String> map = QueryGenerator.getQueryMap(body);

        try{
            return webClient.post()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization",
                            HeaderGenerator.getJwtHeader(accessKey, secretKey, QueryGenerator.convertQueryToHash(map)))
                    .acceptCharset(StandardCharsets.UTF_8)
                    .bodyValue(new Gson().toJson(map))
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> Mono.error(new IllegalStateException("failed post")))
                    .bodyToMono(clazz)
                    .block();

        } catch(Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public void delete(String path, Object queryParams){
        HashMap<String, String> map = QueryGenerator.getQueryMap(queryParams);

        try{
            webClient.delete()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization",
                            HeaderGenerator.getJwtHeader(accessKey, secretKey, QueryGenerator.convertQueryToHash(map)))
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> Mono.error(new IllegalStateException("failed delete")))
                    .toEntity(String.class)
                    .block();
        } catch(Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
