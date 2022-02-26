package com.joebrooks.showmethecoin.infra.upbit.client;

import com.joebrooks.showmethecoin.infra.httpClient.ClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class UpBitClient {
    private final String baseUrl = "https://api.upbit.com/v1";
    private final ClientConfig client;

    public <T> List<LinkedHashMap<String, Object>> getToList(String path, Class<T> clazz) {
        try {
            return client.getClient(baseUrl).get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<LinkedHashMap<String, Object>>>() {})
                    .timeout(Duration.ofMillis(5000))
                    .block();

        } catch (Exception e){
          throw new RuntimeException(e.getMessage(), e);
        }
    }

    public  <T> T get(String path, Class<T> clazz) {
        try {
            return client.getClient(baseUrl).get()
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
            return client.getClient(baseUrl).post()
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
