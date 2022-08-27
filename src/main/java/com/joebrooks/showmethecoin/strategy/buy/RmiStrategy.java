package com.joebrooks.showmethecoin.strategy.buy;

import com.joebrooks.showmethecoin.indicator.rmi.RmiIndicator;
import com.joebrooks.showmethecoin.indicator.rmi.RmiResponse;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RmiStrategy implements IBuyStrategy {

    private final RmiIndicator rmiIndicator;
    private final int BUY_VALUE = 30;
    private final int OFFSET = 5;
    private final int WATCH_COUNT = 5;
    private final int START_INDEX = 1;


    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfoEntity> tradeInfo) {
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
