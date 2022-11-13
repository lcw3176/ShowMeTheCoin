package com.joebrooks.showmethecoin.trade.strategy.policy.rising.buy;

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
public class RisingBuyUsingRmi implements IBuyPolicy {

    private final RmiIndicator rmiIndicator;
    private final int START_INDEX = 2;
    private final int END_COUNT = 5;
    private final int HEAD_INDEX = 1;

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        List<RmiResponse> rmiList = rmiIndicator.getRmi(candleResponses, 14, 5);
        int risingCount = 0;
        int fallingCount = 0;

        for(int i = START_INDEX; i < START_INDEX + END_COUNT; i++){
            if(rmiList.get(i).getRmi() > rmiList.get(i + 1).getRmi()){
                risingCount += END_COUNT + START_INDEX - i;
            } else {
                fallingCount += END_COUNT + START_INDEX - i;
            }
        }
        return risingCount > fallingCount
                && rmiList.get(HEAD_INDEX).getRmi() > rmiList.get(HEAD_INDEX + 1).getRmi();

    }
}
