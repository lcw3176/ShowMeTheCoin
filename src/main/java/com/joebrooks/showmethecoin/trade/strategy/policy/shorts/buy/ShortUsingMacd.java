package com.joebrooks.showmethecoin.trade.strategy.policy.shorts.buy;


import com.joebrooks.showmethecoin.trade.candle.CandleStoreEntity;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdIndicator;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import com.joebrooks.showmethecoin.trade.tradeinfo.TradeInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShortUsingMacd implements IBuyPolicy {

    private final MacdIndicator macdIndicator;
    private static final int START_INDEX = 1;
    private static final int WATCH_COUNT = 40;
    private static final int MINUTE = 1;
    private static final double FALLING_INCLINATION = 1.24;
    private static final double RISING_INCLINATION = 1.04;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<MacdResponse> macdResponseList = macdIndicator.getMacd(candleResponses, MINUTE);

        boolean macdBuy = false;
        boolean signalBuy = false;

        for(int i = START_INDEX; i < WATCH_COUNT + START_INDEX; i++){
            double olderMacdValue = macdResponseList.get(i + 1).getMacd();
            double latestMacdValue = macdResponseList.get(i).getMacd();

            double olderSignalValue = macdResponseList.get(i + 1).getSignal();
            double latestSignalValue = macdResponseList.get(i).getSignal();

            if(Math.abs(latestMacdValue) / Math.abs(olderMacdValue) >= FALLING_INCLINATION
                    && latestMacdValue < 0 && olderMacdValue < 0){
                macdBuy = true;
            }

            if(Math.abs(latestSignalValue) / Math.abs(olderSignalValue) >= FALLING_INCLINATION
                    && latestSignalValue < 0 && olderSignalValue < 0){
                signalBuy = true;
            }


        }

        return macdResponseList.get(0).getSignal() < 0
                && macdResponseList.get(0).getMacd() < 0
                && macdBuy
                && signalBuy
                && Math.abs(macdResponseList.get(0).getMacd()) / Math.abs(macdResponseList.get(1).getMacd()) <= RISING_INCLINATION
                && Math.abs(macdResponseList.get(0).getSignal()) / Math.abs(macdResponseList.get(1).getSignal()) <= RISING_INCLINATION;
    }
}
