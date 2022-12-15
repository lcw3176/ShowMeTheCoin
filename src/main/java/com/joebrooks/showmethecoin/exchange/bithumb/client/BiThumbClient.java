package com.joebrooks.showmethecoin.exchange.bithumb.client;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BiThumbClient {

	private final WebClient biThumbWebClient;
	private static final int READ_TIMEOUT = 5000;

	public <T> T get(String path, Class<T> clazz) {
		try {
			return biThumbWebClient.get()
				.uri(path)
				.accept(MediaType.APPLICATION_JSON)
				.acceptCharset(StandardCharsets.UTF_8)
				.retrieve()
				.onStatus(HttpStatus::isError, response -> response.bodyToMono(String.class)
					.flatMap(errorBody -> Mono.error(new BiThumbException(errorBody))))
				.bodyToMono(clazz)
				.timeout(Duration.ofMillis(READ_TIMEOUT), Mono.error(new BiThumbException("Read Timeout: " + path)))
				.block();

		} catch (Exception e) {
			throw new BiThumbException(e.getMessage(), e);
		}
	}
}
