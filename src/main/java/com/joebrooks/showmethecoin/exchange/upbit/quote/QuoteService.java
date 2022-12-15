package com.joebrooks.showmethecoin.exchange.upbit.quote;

import com.joebrooks.showmethecoin.exchange.upbit.UpBitCoinType;
import com.joebrooks.showmethecoin.exchange.upbit.client.UpBitClient;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class QuoteService {

    private final UpBitClient upBitClient;
    private static final String PATH = "/orderbook";
    private static final String QUERY_NAME = "markets";

    public QuoteResponse getQuote(UpBitCoinType upBitCoinType) {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(PATH)
                .queryParam(QUERY_NAME, upBitCoinType.getName())
                .build();

        return Arrays.asList(upBitClient.get(uri.toString(), QuoteResponse[].class)).get(0);
    }

    public QuoteResponse getQuote(String coinType) {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(PATH)
                .queryParam(QUERY_NAME, coinType)
                .build();

        return Arrays.asList(upBitClient.get(uri.toString(), QuoteResponse[].class)).get(0);
    }

}
