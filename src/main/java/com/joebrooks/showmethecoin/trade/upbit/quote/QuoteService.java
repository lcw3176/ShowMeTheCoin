package com.joebrooks.showmethecoin.trade.upbit.quote;

import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import com.joebrooks.showmethecoin.trade.upbit.client.UpBitClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class QuoteService {

    private final UpBitClient upBitClient;

    public QuoteResponse getQuote(CoinType coinType) {
        String path = "/orderbook";

        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(path)
                .queryParam("markets", coinType.getName())
                .build();


        return Arrays.asList(upBitClient.get(uri.toString(), QuoteResponse[].class)).get(0);
    }

    public QuoteResponse getQuote(String coinType){
        String path = "/orderbook";

        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(path)
                .queryParam("markets", coinType)
                .build();

        return Arrays.asList(upBitClient.get(uri.toString(), QuoteResponse[].class)).get(0);
    }

}
