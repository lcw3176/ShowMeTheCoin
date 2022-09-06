package com.joebrooks.showmethecoin.trade.upbit.ticker;

import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import com.joebrooks.showmethecoin.trade.upbit.client.UpBitClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class TickerService {


    private final UpBitClient upBitClient;

    public TickerResponse getTicker(CoinType coinType){

        UriComponents uri = UriComponentsBuilder.newInstance()
                .path("/ticker")
                .queryParam("markets", coinType.getName())
                .build();


        return Arrays.asList(upBitClient.get(uri.toString(), false, null, TickerRequest.builder()
                .markets(coinType.getName())
                .build(), TickerResponse[].class)).get(0);
    }

}
