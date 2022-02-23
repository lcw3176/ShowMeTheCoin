package com.joebrooks.showmethecoin.global.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class ClientRequest {

    private final WebClient client;

    public  <T> List<T> getAndReceiveList(String path, Class<T> clazz) {
        try {
            return client.get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<T>>() {})
                    .timeout(Duration.ofMillis(5000))
                    .block();

        } catch (Exception e){
          throw new RuntimeException(e.getMessage(), e);
        }
    }

    public  <T> T get(String path, Class<T> clazz) {
        try {
            return client.get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(clazz)
                    .timeout(Duration.ofMillis(5000))
                    .block();

        } catch (Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public  <T> T post(String path, Object body, Class<T> clazz, Supplier<T> onTimeout) {
        try{
            return client.post()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(clazz)
                    .timeout(Duration.ofMillis(5000))
                    .block();
        } catch(Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
