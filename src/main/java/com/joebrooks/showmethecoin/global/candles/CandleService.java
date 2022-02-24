package com.joebrooks.showmethecoin.global.candles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joebrooks.showmethecoin.global.client.ClientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CandleService {

    private final ClientRequest clientRequest;
    private final ObjectMapper mapper = new ObjectMapper();

    public Candle getCandleData() {
        int standardCandleMinute = 1;
        int standardCandleCount = 1;
        Candle temp = null;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String strDate = LocalDateTime.now(ZoneOffset.UTC).format(format);

        Map<String, Object> pathVariableMap = new HashMap<>();

        pathVariableMap.put("candleMinute", standardCandleMinute);


        String uri = UriComponentsBuilder.newInstance()
                .path("/candles/minutes/{candleMinute}")
                .queryParam("market", "KRW-DOGE")
                .queryParam("count", standardCandleCount)
                .queryParam("to", strDate)
                .buildAndExpand(pathVariableMap)
                .toUriString();

        for(LinkedHashMap<String, Object> i : clientRequest.getAndReceiveList(uri, Candle.class)){
            temp = mapper.convertValue(i, Candle.class);
        }

        return temp;
    }
}
