package com.joebrooks.showmethecoin.candles;

import com.joebrooks.showmethecoin.common.upbit.UpBitClient;
import com.joebrooks.showmethecoin.common.upbit.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CandleService {

    private final UpBitClient upBitClient;

    public CandleResponse[] getCandleData(int minute, int candleCount, CoinType coinType) {
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

        return upBitClient.requestCandles(uri);
    }

}
