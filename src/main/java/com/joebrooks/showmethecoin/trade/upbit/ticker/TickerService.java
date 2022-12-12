package com.joebrooks.showmethecoin.trade.upbit.ticker;

import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import com.joebrooks.showmethecoin.trade.upbit.client.UpBitClient;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class TickerService {


    private final UpBitClient upBitClient;

    public List<TickerResponse> getTicker(CoinType... coinTypes){
        List<String> types = Arrays.stream(coinTypes).map(CoinType::getName).collect(Collectors.toList());


        UriComponents uri = UriComponentsBuilder.newInstance()
                .path("/ticker")
                .queryParam("markets", types)
                .build();


        return List.of(upBitClient.get(uri.toString(), TickerResponse[].class));
    }

}
