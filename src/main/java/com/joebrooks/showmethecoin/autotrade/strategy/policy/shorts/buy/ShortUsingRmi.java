package com.joebrooks.showmethecoin.autotrade.strategy.policy.shorts.buy;

import com.joebrooks.showmethecoin.autotrade.indicator.rmi.RmiIndicator;
import com.joebrooks.showmethecoin.autotrade.indicator.rmi.RmiResponse;
import com.joebrooks.showmethecoin.autotrade.strategy.policy.IBuyPolicy;
import com.joebrooks.showmethecoin.repository.candle.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShortUsingRmi implements IBuyPolicy {

    private final RmiIndicator rmiIndicator;
    private static final int BUY_VALUE = 20;
    private static final int OFFSET = 5;
    private static final int WATCH_COUNT = 10;
    private static final int START_INDEX = 1;


    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<RmiResponse> rmiLst = rmiIndicator.getRmi(candleResponses, 60, 5);

        int plusCount = 0;
        int minusCount = 0;

        for(int i = START_INDEX; i < WATCH_COUNT + START_INDEX; i++){
            double temp = rmiLst.get(i).getRmi() - rmiLst.get(i + 1).getRmi();
            if(temp > 0){
                plusCount++;
            } else {
                minusCount++;
            }
        }

        return rmiLst.get(0).getRmi() > BUY_VALUE
                && rmiLst.get(0).getRmi() < BUY_VALUE + OFFSET
                && plusCount > minusCount;
    }
}
