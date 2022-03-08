package com.joebrooks.showmethecoin.global.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;

@Service
@Slf4j
public class LoggingService {

    public void logResponse(ClientResponse response) {
        log.error("Response status: {}", response.statusCode());
        log.error("Response headers: {}", response.headers().asHttpHeaders());
        response.bodyToMono(String.class)
                .publishOn(Schedulers.boundedElastic())
                .subscribe(body -> log.error("Response body: {}", body));
    }

    public void log(Exception e) {
        log.error(Arrays.toString(e.getStackTrace()));
    }

}
