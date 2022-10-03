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
    private static final int WATCH_COUNT = 5;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<MacdResponse> macdResponseList = macdIndicator.getMacd(candleResponses);

        boolean buySignal = false;
        int risingCount = 0;
        int fallingCount = 0;

        for(int i = START_INDEX; i < START_INDEX + WATCH_COUNT; i++){

            double latestSignalValue = Math.round(macdResponseList.get(i).getSignal() * 100) / 100.0;
            double oldSignalValue = Math.round(macdResponseList.get(i + 1).getSignal() * 100) / 100.0;

            double latestMacdValue = Math.round(macdResponseList.get(i).getMacd() * 100) / 100.0;
            double oldMacdValue = Math.round(macdResponseList.get(i + 1).getMacd() * 100) / 100.0;

            if(latestSignalValue > oldSignalValue
                    && latestMacdValue > oldMacdValue){
                risingCount++;
            } else {
                fallingCount++;
            }

        }

        for(int i = START_INDEX; i < START_INDEX + 1; i++){
            double latestMacdValue = Math.round(macdResponseList.get(i).getMacd() * 100) / 100.0 ;
            double latestSignalValue = Math.round(macdResponseList.get(i).getSignal() * 100) / 100.0;

            double priceLine = Math.round((candleResponses.get(i).getTradePrice() / 100 / 4 / 4) * 100) / 100.0;
            double macd = Math.abs(latestMacdValue) / priceLine;
            double signal = Math.abs(latestSignalValue) / priceLine;
            double result = Math.round(Math.min(macd, signal) / Math.max(macd, signal) * 100) / 100.0;

            if (result <= 0.55 && result >= 0.45) {
                buySignal = true;
                break;
            }


        }

        return buySignal
                && risingCount > fallingCount
                && macdResponseList.get(1).getMacd() < macdResponseList.get(0).getMacd()
                && macdResponseList.get(1).getSignal() < macdResponseList.get(0).getSignal();

    }
}
