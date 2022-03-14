package com.joebrooks.showmethecoin.upbitTrade.upbit;

import com.google.gson.Gson;
import com.joebrooks.showmethecoin.global.httpClient.ClientConfig;
import com.joebrooks.showmethecoin.global.log.LoggingService;
import com.joebrooks.showmethecoin.upbitTrade.order.CheckOrderRequest;
import com.joebrooks.showmethecoin.upbitTrade.order.CheckOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class UpBitClient {

    private final String baseUrl = "https://api.upbit.com/v1";
    private final String accessKey = System.getenv("accessKey");
    private final String secretKey = System.getenv("secretKey");

    private final ClientConfig client;
    private final LoggingService loggingService;

    @Value("${upbit.client.timeout}")
    private int timeoutMillis;


    public <T> T[] get(String path, boolean authHeaderRequired, Class<T[]> clazz){
        try {
            return client.getClient(baseUrl, timeoutMillis).get()
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
                    .onStatus(HttpStatus::isError, response -> {
                        loggingService.logResponse(response);
                        return Mono.error(new IllegalStateException("failed get"));
                    })
                    .bodyToMono(clazz)
                    .block();

        } catch (Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <T> T[] get(String path, Object queryParams, Class<T[]> clazz){
        HashMap<String, String> map = QueryGenerator.getQueryMap(queryParams);

        try {
            return client.getClient(baseUrl, timeoutMillis).get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization",
                            HeaderGenerator.getJwtHeader(accessKey, secretKey, QueryGenerator.convertQueryToHash(map)))
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> {
                        loggingService.logResponse(response);
                        return Mono.error(new IllegalStateException("failed get"));
                    })
                    .bodyToMono(clazz)
                    .block();

        } catch (Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <T> T post(String path, Object body, Class<T> clazz){
        HashMap<String, String> map = QueryGenerator.getQueryMap(body);

        try{
            return client.getClient(baseUrl, timeoutMillis).post()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization",
                            HeaderGenerator.getJwtHeader(accessKey, secretKey, QueryGenerator.convertQueryToHash(map)))
                    .acceptCharset(StandardCharsets.UTF_8)
                    .bodyValue(new Gson().toJson(map))
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> {
                        loggingService.logResponse(response);
                        return Mono.error(new IllegalStateException("failed post"));
                    })
                    .bodyToMono(clazz)
                    .block();

        } catch(Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }


//    public CheckOrderResponse[] checkOrder(CheckOrderRequest orderRequest, String path) {
//        HashMap<String, String> map = QueryGenerator.getQueryMap(orderRequest);
//        try{
//            return client.getClient(baseUrl, timeoutMillis).get()
//                    .uri(uriBuilder -> uriBuilder.path(path).queryParam("state", orderRequest.getState()).build())
//                    .accept(MediaType.APPLICATION_JSON)
//                    .header("Authorization",
//                            HeaderGenerator.getJwtHeader(accessKey, secretKey, QueryGenerator.convertQueryToHash(map)))
//                    .acceptCharset(StandardCharsets.UTF_8)
//                    .retrieve()
//                    .onStatus(HttpStatus::isError, response -> {
//                        loggingService.logResponse(response);
//                        return Mono.error(new IllegalStateException("failed order"));
//                    })
//                    .bodyToMono(CheckOrderResponse[].class)
//                    .block();
//
//        } catch(Exception e){
//            throw new RuntimeException(e.getMessage(), e);
//        }
//
//    }

//    public CandleResponse[] requestCandles(String path) {
//        try {
//            return client.getClient(baseUrl, timeoutMillis).get()
//                    .uri(path)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .acceptCharset(StandardCharsets.UTF_8)
//                    .retrieve()
//                    .onStatus(HttpStatus::isError, response -> {
//                        loggingService.logResponse(response);
//                        return Mono.error(new IllegalStateException("failed candles"));
//                    })
//                    .bodyToMono(CandleResponse[].class)
//                    .block();
//
//        } catch (Exception e){
//          throw new RuntimeException(e.getMessage(), e);
//        }
//    }
//
//
//    public AccountResponse[] requestAccountData(String path){
//
//        try {
//            return client.getClient(baseUrl, timeoutMillis).get()
//                    .uri(path)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .header("Authorization", getJwtHeader())
//                    .acceptCharset(StandardCharsets.UTF_8)
//                    .retrieve()
//                    .onStatus(HttpStatus::isError, response -> {
//                        loggingService.logResponse(response);
//                        return Mono.error(new IllegalStateException("failed account"));
//                    })
//                    .bodyToMono(AccountResponse[].class)
//                    .block();
//
//        } catch (Exception e){
//            throw new RuntimeException(e.getMessage(), e);
//        }
//    }



//    public OrderResponse requestOrder(OrderRequest order, String path) {
//        HashMap<String, String> map = queryGenerator.getQueryMap(order);
//        try{
//            return client.getClient(baseUrl, timeoutMillis).post()
//                    .uri(path)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .header("Authorization", getJwtHeader(queryGenerator.convertQueryString(map)))
//                    .acceptCharset(StandardCharsets.UTF_8)
//                    .bodyValue(new Gson().toJson(map))
//                    .retrieve()
//                    .onStatus(HttpStatus::isError, response -> {
//                        loggingService.logResponse(response);
//                        return Mono.error(new IllegalStateException("failed order"));
//                    })
//                    .bodyToMono(OrderResponse.class)
//                    .block();
//
//        } catch(Exception e){
//            throw new RuntimeException(e.getMessage(), e);
//        }
//
//    }



//    public MultiValueMap<String, String> getMaps(Object obj) {
//        ObjectMapper mapper = new ObjectMapper();
//        TypeReference<Map<String,String>> typeRef = new TypeReference<Map<String,String >>(){};
//
//        Map<String, String> result = mapper.convertValue(obj, typeRef);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.setAll(result);
//
//        return params;
//    }

}
