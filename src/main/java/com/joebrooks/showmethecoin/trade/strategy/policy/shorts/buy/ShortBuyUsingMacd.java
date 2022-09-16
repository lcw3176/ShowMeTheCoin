package com.joebrooks.showmethecoin.trade.strategy.policy.shorts.buy;


import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdIndicator;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShortBuyUsingMacd implements IBuyPolicy {

    private final MacdIndicator macdIndicator;
    private static final int START_INDEX = 1;
    private static final int WATCH_COUNT = 3;


    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<MacdResponse> macdResponseList = macdIndicator.getMacd(candleResponses);
        CandleStoreEntity candleStoreEntity = candleResponses.get(0);

        boolean macdBuy = false;
        boolean signalBuy = false;
//        int risingCount = 0;
//        int fallingCount = 0;
//        boolean priceBuy = false;
        double macdThresholdMin;
        double macdThresholdMax;

        double signalThresholdMin;
        double signalThresholdMax;

        if(candleStoreEntity.getTradePrice() >= 1000000){
            macdThresholdMin = 1.19;
            macdThresholdMax = 1.23;

            signalThresholdMin = 1.19; /// 1.19, 1.25, 1.23, 1.29, 1.26 O, 1.27 O, 1.28 O, 1.32 X
            signalThresholdMax = 1.23; /// 1.23, 1.29, 1.27, 1.33, 1.30 O, 1.31 O, 1.32 O, 1.36 X
        } else {
//            macdThresholdMin = 1.06;
//            macdThresholdMax = 1.10;
//
//            signalThresholdMin = 1.02;
//            signalThresholdMax = 1.06;

//            macdThresholdMin = 1.42;
//            macdThresholdMax = 1.5;
//
//            signalThresholdMin = 1.12;
//            signalThresholdMax = 1.2;


            macdThresholdMin = 0.80;  //
            macdThresholdMax = 0.90;

            signalThresholdMin = 0.72;
            signalThresholdMax = 0.82;


//            macdThresholdMin = 0.95;
//            macdThresholdMax = 1.15;
//
//            signalThresholdMin = 0.61;
//            signalThresholdMax = 0.81;
        }

        boolean buySignal = false;
        for(int i = START_INDEX; i < WATCH_COUNT + START_INDEX; i++){
            double olderMacdValue = macdResponseList.get(i + 1).getMacd();
            double latestMacdValue = macdResponseList.get(i).getMacd();

            double olderSignalValue = macdResponseList.get(i + 1).getSignal();
            double latestSignalValue = macdResponseList.get(i).getSignal();

            double priceLine = candleResponses.get(i).getTradePrice() / 100 / 4 / 2;
//
//            if(olderMacdValue < 0
//                    && latestMacdValue < 0
//                    && Math.abs(latestMacdValue) - Math.abs(olderMacdValue) < 0){
//                risingCount++;
//            } else {
//                fallingCount++;
//            }

            if(Math.abs(latestMacdValue) / Math.abs(priceLine) >= macdThresholdMin
                    && Math.abs(latestMacdValue) / Math.abs(priceLine) <= macdThresholdMax
                    && latestMacdValue < 0){
                macdBuy = true;
            }

            if(Math.abs(latestSignalValue) / Math.abs(priceLine) >= signalThresholdMin
                    && Math.abs(latestSignalValue) / Math.abs(priceLine) <= signalThresholdMax
                    && latestSignalValue < 0){
                signalBuy = true;
            }


        }

        return macdResponseList.get(0).getSignal() < 0
                && macdResponseList.get(0).getMacd() < 0
                && signalBuy
                && macdBuy;
    }
}
