package com.joebrooks.showmethecoin.exchange.coinone.client;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CoinOneClient {


    private final WebClient coinOneWebClient;
    private static final int READ_TIMEOUT = 5000;


    public <T> T get(String path, Class<T> clazz) {
        try {
            return coinOneWebClient.get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new CoinOneException(errorBody))))
                    .bodyToMono(clazz)
                    .timeout(Duration.ofMillis(READ_TIMEOUT), Mono.error(new CoinOneException("Read Timeout: " + path)))
                    .block();

        } catch (Exception e) {
            throw new CoinOneException(e.getMessage(), e);
        }
    }

}
