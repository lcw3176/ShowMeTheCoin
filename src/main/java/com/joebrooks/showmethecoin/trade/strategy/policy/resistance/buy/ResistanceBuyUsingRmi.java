package com.joebrooks.showmethecoin.trade.strategy.policy.resistance.buy;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.rmi.RmiIndicator;
import com.joebrooks.showmethecoin.trade.indicator.rmi.RmiResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResistanceBuyUsingRmi implements IBuyPolicy {

    private final RmiIndicator rmiIndicator;
    private static final int START_INDEX = 1;
    private static final int WATCH_COUNT = 3;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<RmiResponse> rmiList = rmiIndicator.getRmi(candleResponses, 60, 5);
        int risingCount = 0;
        int fallingCount = 0;

        for(int i = START_INDEX; i < WATCH_COUNT + START_INDEX; i++){
            if(rmiList.get(i).getRmi() - rmiList.get(i + 1).getRmi() > 0){
                risingCount++;
            } else {
                fallingCount++;
            }
        }

        return risingCount > fallingCount;

    }
}
