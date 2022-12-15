package com.joebrooks.showmethecoin.exchange.coinone.ticker;

import com.joebrooks.showmethecoin.exchange.coinone.client.CoinOneClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


@Service
@RequiredArgsConstructor
public class CoinOneTickerService {

    private final CoinOneClient coinOneClient;
    private static final String PATH = "/ticker_new/krw";

    public List<TickerResponse> getTickers() {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(PATH)
                .build();

        return coinOneClient.get(uri.toString(), Ticker.class).getTickers();
    }


}
