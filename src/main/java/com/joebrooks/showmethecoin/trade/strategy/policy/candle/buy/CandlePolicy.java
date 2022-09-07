package com.joebrooks.showmethecoin.trade.strategy.policy.candle.buy;


import com.joebrooks.showmethecoin.trade.candle.CandleStoreEntity;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import com.joebrooks.showmethecoin.trade.tradeinfo.TradeInfoEntity;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CandlePolicy implements IBuyPolicy {



    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        int buyCount = 3;
        int count = 0;

        for(int i = 0; i < buyCount; i++){
            CandleStoreEntity candle = candleResponses.get(i);

            double price = candle.getTradePrice() - candle.getOpeningPrice();

            if(price < 0) {
                count++;
            }
        }

        return count >= buyCount;
    }

}
