package com.joebrooks.showmethecoin.trade.strategy.policy.resistance.buy;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResistanceBuyUsingCandle implements IBuyPolicy {

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {

        int risingCount = 0;
        int fallingCount = 0;

        for(int i = 0; i < 3; i++){
            if(candleResponses.get(i).getTradePrice() < candleResponses.get(i + 1).getTradePrice()){
                fallingCount++;
            } else {
                risingCount++;
            }
        }

        return risingCount > fallingCount;
    }
}
