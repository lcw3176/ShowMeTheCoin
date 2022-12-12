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
    private static final String PATH = "/orderbook";
    private static final String QUERY_NAME = "markets";

    public QuoteResponse getQuote(CoinType coinType) {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(PATH)
                .queryParam(QUERY_NAME, coinType.getName())
                .build();


        return Arrays.asList(upBitClient.get(uri.toString(), QuoteResponse[].class)).get(0);
    }

    public QuoteResponse getQuote(String coinType){
        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(PATH)
                .queryParam(QUERY_NAME, coinType)
                .build();

        return Arrays.asList(upBitClient.get(uri.toString(), QuoteResponse[].class)).get(0);
    }

}
