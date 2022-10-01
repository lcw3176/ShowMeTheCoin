package com.joebrooks.showmethecoin.trade.strategy.policy.shorts.buy;


import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdIndicator;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShortBuyUsingMacd implements IBuyPolicy {

    private final MacdIndicator macdIndicator;
    private static final int START_INDEX = 1;
    private static final int WATCH_COUNT = 8;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<MacdResponse> macdResponseList = macdIndicator.getMacd(candleResponses);
        CandleStoreEntity candleStoreEntity = candleResponses.get(0);

        boolean macdBuy = false;
        boolean signalBuy = false;

//        double macdThresholdMin;
//        double macdThresholdMax;
//
//        double signalThresholdMin;
//        double signalThresholdMax;

//        if(candleStoreEntity.getTradePrice() >= 1000000){
//
////            macdThresholdMin = 1.532;
////            macdThresholdMax = 1.732;
////
////            signalThresholdMin = 1.232;
////            signalThresholdMax = 1.432;
//
//            macdThresholdMin = 0.97;  //
//            macdThresholdMax = 1.07;
//
//            signalThresholdMin = 1.31;
//            signalThresholdMax = 1.41;
//        } else {
////            macdThresholdMin = 1.06;
////            macdThresholdMax = 1.10;
////
////            signalThresholdMin = 1.02;
////            signalThresholdMax = 1.06;
//
////            macdThresholdMin = 1.42;
////            macdThresholdMax = 1.5;
////
////            signalThresholdMin = 1.12;
////            signalThresholdMax = 1.2;
//
//
////            macdThresholdMin = 0.80;  //
////            macdThresholdMax = 0.90;
////
////            signalThresholdMin = 0.72;
////            signalThresholdMax = 0.82;
//
//
//
////            macdThresholdMin = 0.95;
////            macdThresholdMax = 1.15;
////
////            signalThresholdMin = 0.61;
////            signalThresholdMax = 0.81;
////
////            macdThresholdMin = 1.532;
////            macdThresholdMax = 1.732;
////
////            signalThresholdMin = 1.232;
////            signalThresholdMax = 1.432;
//
////            macdThresholdMin = 0.97;  // 안정적
////            macdThresholdMax = 1.07;
////
////            signalThresholdMin = 1.31;
////            signalThresholdMax = 1.41;
//
//
////            macdThresholdMin = 1.64;  //
////            macdThresholdMax = 1.84;
////
////            signalThresholdMin = 2.03;
////            signalThresholdMax = 2.23;
//
//            macdThresholdMin = 9.3;  //
//            macdThresholdMax = 9.5;
//
//            signalThresholdMin = 9.8;
//            signalThresholdMax = 10;
//
//        }

        int risingCount = 0;
        int fallingCount = 0;

        for(int i = START_INDEX; i < START_INDEX + WATCH_COUNT; i++){

            double latestSignalValue = Math.round((macdResponseList.get(i).getSignal() * 100) / 100);
            double oldSignalValue = Math.round((macdResponseList.get(i + 1).getSignal() * 100) / 100);

            double latestMacdValue = Math.round((macdResponseList.get(i).getMacd() * 100) / 100) ;
            double oldMacdValue = Math.round((macdResponseList.get(i + 1).getMacd() * 100) / 100) ;

            if(latestSignalValue > oldSignalValue
                    && latestSignalValue < 0
                    && oldSignalValue < 0
                    && latestMacdValue < 0
                    && oldMacdValue < 0
                    && latestMacdValue > oldMacdValue){
                risingCount++;
            } else {
                fallingCount++;
            }

        }

        for(int i = START_INDEX; i < START_INDEX + 1; i++){
            double latestMacdValue = Math.round((macdResponseList.get(i).getMacd() * 100) / 100) ;
//            double oldMacdValue = Math.round((macdResponseList.get(i + 1).getMacd() * 100) / 100) ;
            double latestSignalValue = Math.round((macdResponseList.get(i).getSignal() * 100) / 100);
//            double oldSignalValue = Math.round((macdResponseList.get(i + 1).getSignal() * 100) / 100);

            double priceLine = Math.round((candleResponses.get(i).getTradePrice() / 100 / 4 / 4) * 100 / 100);

//            if(Math.abs(latestMacdValue) / priceLine >= macdThresholdMin
//                    && Math.abs(latestMacdValue) / priceLine <= macdThresholdMax){
//                macdBuy = true;
//            }
//
//            if(Math.abs(latestSignalValue) / priceLine >= signalThresholdMin
//                    && Math.abs(latestSignalValue) / priceLine <= signalThresholdMax){
//                signalBuy = true;
//            }
//            1.15  1.2

//            log.info("time: {}, macd: {}",candleResponses.get(i).getDateKst(), (Math.abs(latestMacdValue) / priceLine) / (Math.abs(latestSignalValue) / priceLine));

           if(  (Math.abs(latestMacdValue) / priceLine) / (Math.abs(latestSignalValue) / priceLine) <= 0.7
                && latestMacdValue < 0
                && latestSignalValue < 0){

                macdBuy = true;
                signalBuy = true;
            }


        }

        return macdResponseList.get(0).getMacd() < 0
                && macdResponseList.get(0).getSignal() < 0
                && macdBuy
                && signalBuy
                && risingCount > fallingCount
                && macdResponseList.get(1).getMacd() < macdResponseList.get(0).getMacd()
                && macdResponseList.get(1).getSignal() < macdResponseList.get(0).getSignal();

    }
}
