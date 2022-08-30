package com.joebrooks.showmethecoin.autotrade.upbit.quote;

import com.joebrooks.showmethecoin.autotrade.upbit.CoinType;
import com.joebrooks.showmethecoin.autotrade.upbit.client.UpBitClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class QuoteService {

    private final UpBitClient upBitClient;

    public QuoteResponse getQuote(CoinType coinType){
        String path = "/orderbook";

        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(path)
                .queryParam("markets", coinType.getName())
                .build();


        return Arrays.asList(upBitClient.get(uri.toString(), false, QuoteRequest.builder()
                .markets(coinType.getName())
                .build(), QuoteResponse[].class)).get(0);
    }

    public QuoteResponse getQuote(String coinType){
        String path = "/orderbook";

        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(path)
                .queryParam("markets", coinType)
                .build();

        return Arrays.asList(upBitClient.get(uri.toString(), false, QuoteRequest.builder()
                .markets(coinType)
                .build(), QuoteResponse[].class)).get(0);
    }

}
