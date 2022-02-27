package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.candles.CandleResponse;
import com.joebrooks.showmethecoin.candles.CandleService;
import com.joebrooks.showmethecoin.common.upbit.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component(StrategyType.RSI)
@RequiredArgsConstructor
public class RsiStrategy implements IStrategy {

    private final CandleService candleService;

    private Strategy<RSI> getRsi(CoinType coinType){
        int days = 14;
        List<CandleResponse> val = new LinkedList<>();

        val.addAll(Arrays.asList(candleService.getCandleData(1, days + 1, coinType)));

        CandleResponse recentValue = val.get(0);

        Strategy<RSI> strategy = new Strategy<>();
        strategy.setType(StrategyType.RSI);
        strategy.setDateKst(recentValue.getDateKst());
        strategy.setTradePrice(recentValue.getTradePrice());

//        for(int i = 0; i < val.size() - days; i++){
//            double tempUp = 0;
//            double tempDown = 0;
//
//            for(int j = 0; j < days; j++){
//                double diff = val.get(i + j).getTradePrice() - val.get(i + j + 1).getTradePrice();
//
//                if(diff > 0){
//                    tempUp += diff;
//                } else {
//                    tempDown += Math.abs(diff);
//                }
//            }
//
//            double rs = (tempUp / days) / (tempDown / days);
//            double rsi = rs / (1 + rs);
//
//            if(rsi <= 0.3){
//                System.out.println("구매 : " + val.get(i).getDateKst() + " " + val.get(i).getTradePrice());
//            } else if(rsi >= 0.68){
//                System.out.println("판매 : " + val.get(i).getDateKst() + " " + val.get(i).getTradePrice());
//            }
//        }

        double tempUp = 0;
        double tempDown = 0;

        for(int j = 0; j < days; j++){
            double diff = val.get(j).getTradePrice() - val.get(j + 1).getTradePrice();

            if(diff > 0){
                tempUp += diff;
            } else {
                tempDown += Math.abs(diff);
            }
        }

        double rs = (tempUp / days) / (tempDown / days);
        double rsi = rs / (1 + rs);

        if(rsi <= 0.2){
            strategy.setRecommend(RecommendAction.BUY);
            strategy.setDetailInfo(RSI.builder()
                    .rsi(rsi)
                    .recommend(RecommendAction.BUY)
                    .build());
        } else if (rsi >= 0.7){
            strategy.setRecommend(RecommendAction.SELL);
            strategy.setDetailInfo(RSI.builder()
                    .rsi(rsi)
                    .recommend(RecommendAction.SELL)
                    .build());
        } else {
            strategy.setRecommend(RecommendAction.STAY);
            strategy.setDetailInfo(RSI.builder()
                    .rsi(rsi)
                    .recommend(RecommendAction.STAY)
                    .build());
        }

        return strategy;
    }

    @Override
    public Strategy<RSI> execute(CoinType coinType) {

        return getRsi(coinType);
    }
}
