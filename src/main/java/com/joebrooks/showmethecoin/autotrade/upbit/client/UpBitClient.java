package com.joebrooks.showmethecoin.autotrade.upbit.client;

import com.google.gson.Gson;
import com.joebrooks.showmethecoin.repository.userkey.UserKeyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class UpBitClient {


    private final WebClient upBitWebClient;
    private static final String AUTH_HEADER = "Authorization";
    private static final int READ_TIMEOUT = 5000;

    public <T> T[] get(String path, boolean authHeaderRequired, UserKeyEntity userKey, Class<T[]> clazz){
        try {

            return upBitWebClient.get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .headers(httpHeaders -> {
                        if(authHeaderRequired) {
                            httpHeaders.set(AUTH_HEADER, HeaderGenerator.getJwtHeader(userKey.getAccessKey(), userKey.getSecretKey()));
                        }
                    })
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> Mono.error(new UpBitException("failed get")))
                    .bodyToMono(clazz)
                    .timeout(Duration.ofMillis(READ_TIMEOUT), Mono.error(new UpBitException("Read Timeout: " + path)))
                    .block();

        } catch (Exception e){
            throw new UpBitException(e.getMessage(), e);
        }
    }

    public <T> T[] get(String path, boolean authHeaderRequired, UserKeyEntity userKey, Object queryParams, Class<T[]> clazz){
        HashMap<String, String> map = QueryGenerator.getQueryMap(queryParams);

        try {
            return upBitWebClient.get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .headers(httpHeaders -> {
                        if(authHeaderRequired){
                            httpHeaders.set(AUTH_HEADER,
                                    HeaderGenerator.getJwtHeader(userKey.getAccessKey(), userKey.getSecretKey(), QueryGenerator.convertQueryToHash(map)));
                        }
                    })
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> Mono.error(new UpBitException("failed get")))
                    .bodyToMono(clazz)
                    .timeout(Duration.ofMillis(READ_TIMEOUT), Mono.error(new UpBitException("Read Timeout: " + map)))
                    .block();

        } catch (Exception e){
            throw new UpBitException(map.toString(), e);
        }
    }

    public <T> T post(String path, Object body, UserKeyEntity userKey, Class<T> clazz){
        HashMap<String, String> map = QueryGenerator.getQueryMap(body);

        try{
            return upBitWebClient.post()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER,
                            HeaderGenerator.getJwtHeader(userKey.getAccessKey(), userKey.getSecretKey(), QueryGenerator.convertQueryToHash(map)))
                    .acceptCharset(StandardCharsets.UTF_8)
                    .bodyValue(new Gson().toJson(map))
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> Mono.error(new UpBitException("failed post")))
                    .bodyToMono(clazz)
                    .timeout(Duration.ofMillis(READ_TIMEOUT), Mono.error(new UpBitException("Read Timeout: " + map)))
                    .block();

        } catch(Exception e){
            throw new UpBitException(map.toString(), e);
        }
    }


    public void delete(String path, UserKeyEntity userKey, Object queryParams){
        HashMap<String, String> map = QueryGenerator.getQueryMap(queryParams);

        try{
            upBitWebClient.delete()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER,
                            HeaderGenerator.getJwtHeader(userKey.getAccessKey(), userKey.getSecretKey(), QueryGenerator.convertQueryToHash(map)))
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> Mono.error(new UpBitException("failed delete")))
                    .toEntity(String.class)
                    .timeout(Duration.ofMillis(READ_TIMEOUT), Mono.error(new UpBitException("Read Timeout: " + map)))
                    .block();
        } catch(Exception e){
            throw new UpBitException(map.toString(), e);
        }
    }

}
