package com.joebrooks.showmethecoin.infra.upbit.candles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joebrooks.showmethecoin.infra.upbit.client.UpBitClient;
import com.joebrooks.showmethecoin.infra.upbit.coin.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CandleService {

    private final UpBitClient clientRequest;
    private final ObjectMapper mapper = new ObjectMapper();

    public void getCandleData(int minute, int candleCount, CoinType coinType, Consumer<Candle> consumer) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String strDate = LocalDateTime.now(ZoneOffset.UTC).format(format);

        Map<String, Object> pathVariableMap = new HashMap<>();

        pathVariableMap.put("candleMinute", minute);


        String uri = UriComponentsBuilder.newInstance()
                .path("/candles/minutes/{candleMinute}")
                .queryParam("market", coinType.getName())
                .queryParam("count", candleCount)
                .queryParam("to", strDate)
                .buildAndExpand(pathVariableMap)
                .toUriString();

        for(LinkedHashMap<String, Object> i : clientRequest.getToList(uri, Candle.class)){
            consumer.accept(mapper.convertValue(i, Candle.class));
        }
    }

}
