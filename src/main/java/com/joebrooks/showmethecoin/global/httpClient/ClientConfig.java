package com.joebrooks.showmethecoin.global.httpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
public class ClientConfig {

	@Bean
	public WebClient upBitWebClient() {
		String baseUrl = "https://api.upbit.com/v1";

		return WebClient.builder()
			.baseUrl(baseUrl)
			.defaultHeaders(headers -> {
				headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			})
			.clientConnector(new ReactorClientHttpConnector(httpClient()))
			.build();
	}

	@Bean
	public WebClient coinOneWebClient() {

		String baseUrl = "https://api.coinone.co.kr/public/v2";

		return WebClient.builder()
			.baseUrl(baseUrl)
			.defaultHeaders(headers -> {
				headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			})
			.clientConnector(new ReactorClientHttpConnector(httpClient()))
			.build();
	}

	@Bean
	public WebClient biThumbWebClient() {

		String baseUrl = "https://api.bithumb.com/public";

		return WebClient.builder()
			.baseUrl(baseUrl)
			.defaultHeaders(headers -> {
				headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			})
			.clientConnector(new ReactorClientHttpConnector(httpClient()))
			.build();
	}

	public HttpClient httpClient() {
		int timeoutMillis = 10000;

		return HttpClient.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutMillis)
			.responseTimeout(Duration.ofMillis(timeoutMillis))
			.doOnConnected(conn ->
				conn.addHandlerLast(new ReadTimeoutHandler(timeoutMillis, TimeUnit.MILLISECONDS))
					.addHandlerLast(new WriteTimeoutHandler(timeoutMillis, TimeUnit.MILLISECONDS))
			);
	}

}
