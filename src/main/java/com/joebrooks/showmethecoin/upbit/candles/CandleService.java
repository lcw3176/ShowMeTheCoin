package com.joebrooks.showmethecoin.upbit.candles;

import com.joebrooks.showmethecoin.upbit.client.UpBitClient;
import com.joebrooks.showmethecoin.upbit.client.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CandleService {

    private final UpBitClient upBitClient;
    @Value("${upbit.candle.minute}")
    private int minute;

    @Value("${upbit.candle.count}")
    private int count;

    public List<CandleResponse> getCandles(CoinType coinType) {
        Map<String, Object> pathVariableMap = new HashMap<>();

        pathVariableMap.put("candleMinute", minute);

        String uri = UriComponentsBuilder.newInstance()
                .path("/candles/minutes/{candleMinute}")
                .queryParam("market", coinType.getName())
                .queryParam("count", count)
                .buildAndExpand(pathVariableMap)
                .toUriString();

        return Arrays.asList(upBitClient.get(uri, false, CandleResponse[].class));
    }

}
