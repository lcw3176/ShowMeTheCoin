package com.joebrooks.showmethecoin.trade.upbit.candles;

import com.joebrooks.showmethecoin.repository.candlestore.CandleMinute;
import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreService;
import com.joebrooks.showmethecoin.trade.CompanyType;
import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import com.joebrooks.showmethecoin.trade.upbit.client.UpBitClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandleService {

    private final UpBitClient upBitClient;
    private final CandleStoreService candleStoreService;


    public List<CandleStoreEntity> getCandles(CoinType coinType, CandleMinute minute, int count) {

        if(candleStoreService.getSize(coinType, minute) == 0){
            List<CandleResponse> candleResponseList = request(coinType, minute, count);

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
            List<CandleResponse> candleResponseList = request(coinType, minute, 2);
            CandleResponse response = candleResponseList.get(0);
            List<CandleStoreEntity> candleStoreEntityList = candleStoreService.getSortedCandleFromRecent(coinType, minute);

            if(candleStoreEntityList.get(0)
                    .getDateKst().equals(response.getDateKst())){
                CandleStoreEntity candleStore = candleStoreEntityList.get(0);

                candleStore.changeAccTradePrice(response.getAccTradePrice());
                candleStore.changeAccTradeVolume(response.getAccTradeVolume());

                candleStore.changeTimeStamp(response.getTimeStamp());

                candleStore.changeHighPrice(response.getHighPrice());
                candleStore.changeLowPrice(response.getLowPrice());
                candleStore.changeTradePrice(response.getTradePrice());
                candleStoreService.save(candleStore);
            } else {

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

                CandleStoreEntity beforeCandle = candleStoreEntityList.get(1);

                beforeCandle.changeAccTradePrice(response.getAccTradePrice());
                beforeCandle.changeAccTradeVolume(response.getAccTradeVolume());

                beforeCandle.changeTimeStamp(response.getTimeStamp());

                beforeCandle.changeHighPrice(response.getHighPrice());
                beforeCandle.changeLowPrice(response.getLowPrice());
                beforeCandle.changeTradePrice(response.getTradePrice());

                candleStoreService.save(beforeCandle);
                candleStoreService.removeMostOlderCandle(response.getMarket(), minute);
            }
        }

        return candleStoreService.getCandles(coinType, minute);
    }

    public List<CandleStoreEntity> getCandles(CoinType coinType, String to, CandleMinute minute) {

        return request(coinType, to, minute).stream().map(i ->
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
                                .build())
                .sorted(Comparator.comparing(CandleStoreEntity::getDateKst).reversed())
                .collect(Collectors.toList());
    }

    private List<CandleResponse> request(CoinType coinType, CandleMinute minute, int count){
        Map<String, Object> pathVariableMap = new HashMap<>();
        pathVariableMap.put("candleMinute", minute.getValue());
        String uri = UriComponentsBuilder.newInstance()
                .path("/candles/minutes/{candleMinute}")
                .queryParam("market", coinType.getName())
                .queryParam("count", count)
                .buildAndExpand(pathVariableMap)
                .toUriString();


        return List.of(upBitClient.get(uri, CandleResponse[].class));
    }

    private List<CandleResponse> request(CoinType coinType, String to, CandleMinute minute){
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

        return List.of(upBitClient.get(uri, CandleResponse[].class));
    }

}
