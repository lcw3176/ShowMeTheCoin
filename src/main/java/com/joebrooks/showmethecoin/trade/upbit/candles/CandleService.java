package com.joebrooks.showmethecoin.trade.upbit.candles;

import com.joebrooks.showmethecoin.trade.upbit.client.CoinType;
import com.joebrooks.showmethecoin.trade.upbit.client.UpBitClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CandleService {

    private final UpBitClient upBitClient;

    private List<CandleResponse> responses = null;
    public List<CandleResponse> getCandles(CoinType coinType) {
        int minute = 1;

        if(responses == null){
            responses = request(coinType, minute, 200);

        } else {
            CandleResponse temp = request(coinType, minute, 1).get(0);

            if(responses.get(0).getDateKst().equals(temp.getDateKst())){
                responses.set(0, temp);
            } else {
                responses.add(0, temp);
                responses.remove(200);
            }
        }

        return responses;
    }

    public List<CandleResponse> getCandles(CoinType coinType, String to, int minute) {
        return request(coinType, to, minute);
    }

    private List<CandleResponse> request(CoinType coinType, int minute, int count){
        Map<String, Object> pathVariableMap = new HashMap<>();

        pathVariableMap.put("candleMinute", minute);

        String uri = UriComponentsBuilder.newInstance()
                .path("/candles/minutes/{candleMinute}")
                .queryParam("market", coinType.getName())
                .queryParam("count", count)
                .buildAndExpand(pathVariableMap)
                .toUriString();

        return new LinkedList<>(Arrays.asList(upBitClient.get(uri, false, CandleResponse[].class)));
    }

    private List<CandleResponse> request(CoinType coinType, String to, int minute){
        int count = 200;

        Map<String, Object> pathVariableMap = new HashMap<>();

        pathVariableMap.put("candleMinute", minute);

        String uri = UriComponentsBuilder.newInstance()
                .path("/candles/minutes/{candleMinute}")
                .queryParam("market", coinType.getName())
                .queryParam("count", count)
                .queryParam("to", to)
                .buildAndExpand(pathVariableMap)
                .toUriString();

        return new LinkedList<>(Arrays.asList(upBitClient.get(uri, false, CandleResponse[].class)));
    }

}
