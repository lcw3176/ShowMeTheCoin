package com.joebrooks.showmethecoin.autotrade.upbit.candles;

import com.joebrooks.showmethecoin.repository.CompanyType;
import com.joebrooks.showmethecoin.repository.candle.CandleMinute;
import com.joebrooks.showmethecoin.repository.candle.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.candle.CandleStoreService;
import com.joebrooks.showmethecoin.autotrade.upbit.CoinType;
import com.joebrooks.showmethecoin.autotrade.upbit.client.UpBitClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CandleService {

    private final UpBitClient upBitClient;
    private final CandleStoreService candleStoreService;

    @PostConstruct
    private void init(){
        candleStoreService.deleteAll();
    }

    public List<CandleStoreEntity> getCandles(CoinType coinType, CandleMinute minute, int count) {
        List<CandleResponse> candleResponseList = request(coinType, minute, count);

        if(candleStoreService.getSize(coinType, minute) == 0){
            candleResponseList.forEach(i ->
                    candleStoreService.save(
                            CandleStoreEntity.builder()
                                    .market(i.getMarket())
                                    .dateUtc(i.getDateUtc())
                                    .dateKst(i.getDateKst())
                                    .openingPrice(i.getOpeningPrice())
                                    .highPrice(i.getHighPrice())
                                    .lowPrice(i.getLowPrice())
                                    .tradePrice(i.getTradePrice())
                                    .timeStamp(i.getTimeStamp())
                                    .accTradePrice(i.getAccTradePrice())
                                    .accTradeVolume(i.getAccTradeVolume())
                                    .unit(i.getUnit())
                                    .companyType(CompanyType.UPBIT)
                                    .candleMinute(minute)
                                    .build()));
        } else {
            CandleResponse response = candleResponseList.get(0);

            if(candleStoreService.getSortedCandleFromRecent(coinType, minute).get(0)
                    .getDateKst().equals(response.getDateKst())){

                candleStoreService.changeRecentCandle(
                        CandleStoreEntity.builder()
                                .market(response.getMarket())
                                .dateUtc(response.getDateUtc())
                                .dateKst(response.getDateKst())
                                .openingPrice(response.getOpeningPrice())
                                .highPrice(response.getHighPrice())
                                .lowPrice(response.getLowPrice())
                                .tradePrice(response.getTradePrice())
                                .timeStamp(response.getTimeStamp())
                                .accTradePrice(response.getAccTradePrice())
                                .accTradeVolume(response.getAccTradeVolume())
                                .unit(response.getUnit())
                                .candleMinute(minute)
                                .companyType(CompanyType.UPBIT)
                                .build());
            } else {
                candleStoreService.removeMostOlderCandle(response.getMarket(), minute);
                candleStoreService.save(
                        CandleStoreEntity.builder()
                                .market(response.getMarket())
                                .dateUtc(response.getDateUtc())
                                .dateKst(response.getDateKst())
                                .openingPrice(response.getOpeningPrice())
                                .highPrice(response.getHighPrice())
                                .lowPrice(response.getLowPrice())
                                .tradePrice(response.getTradePrice())
                                .timeStamp(response.getTimeStamp())
                                .accTradePrice(response.getAccTradePrice())
                                .accTradeVolume(response.getAccTradeVolume())
                                .unit(response.getUnit())
                                .candleMinute(minute)
                                .companyType(CompanyType.UPBIT)
                                .build());
            }
        }

        return candleStoreService.getCandles(coinType, minute);
    }

    public List<CandleStoreEntity> getCandles(CoinType coinType, String to, CandleMinute minute) {
        return request(coinType, to, minute);
    }

    private List<CandleResponse> request(CoinType coinType, CandleMinute minute, int count){
        List<CandleStoreEntity> candleStoreEntityList = candleStoreService.getSortedCandleFromRecent(coinType, minute);

        Map<String, Object> pathVariableMap = new HashMap<>();
        pathVariableMap.put("candleMinute", minute.getValue());
        String uri;

        if(candleStoreEntityList.size() < count){
            uri = UriComponentsBuilder.newInstance()
                    .path("/candles/minutes/{candleMinute}")
                    .queryParam("market", coinType.getName())
                    .queryParam("count", count)
                    .buildAndExpand(pathVariableMap)
                    .toUriString();

        } else {
            uri = UriComponentsBuilder.newInstance()
                    .path("/candles/minutes/{candleMinute}")
                    .queryParam("market", coinType.getName())
                    .queryParam("count", 1)
                    .buildAndExpand(pathVariableMap)
                    .toUriString();
        }

        return List.of(upBitClient.get(uri, false, null, CandleResponse[].class));
    }

    private List<CandleStoreEntity> request(CoinType coinType, String to, CandleMinute minute){
        int count = 200;

        Map<String, Object> pathVariableMap = new HashMap<>();

        pathVariableMap.put("candleMinute", minute.getValue());

        String uri = UriComponentsBuilder.newInstance()
                .path("/candles/minutes/{candleMinute}")
                .queryParam("market", coinType.getName())
                .queryParam("count", count)
                .queryParam("to", to)
                .buildAndExpand(pathVariableMap)
                .toUriString();

        return Stream.of(upBitClient.get(uri, false, null, CandleResponse[].class))
                .map(i ->
                        CandleStoreEntity.builder()
                        .market(i.getMarket())
                        .dateUtc(i.getDateUtc())
                        .dateKst(i.getDateKst())
                        .openingPrice(i.getOpeningPrice())
                        .highPrice(i.getHighPrice())
                        .lowPrice(i.getLowPrice())
                        .tradePrice(i.getTradePrice())
                        .timeStamp(i.getTimeStamp())
                        .accTradePrice(i.getAccTradePrice())
                        .accTradeVolume(i.getAccTradeVolume())
                        .unit(i.getUnit())
                        .candleMinute(minute)
                        .build())
                .sorted(Comparator.comparing(CandleStoreEntity::getDateKst).reversed())
                .collect(Collectors.toList());
    }

}
