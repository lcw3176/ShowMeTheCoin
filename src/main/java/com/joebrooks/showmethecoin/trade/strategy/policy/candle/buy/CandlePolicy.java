package com.joebrooks.showmethecoin.trade.strategy.policy.candle.buy;


import com.joebrooks.showmethecoin.trade.candle.CandleStoreEntity;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import com.joebrooks.showmethecoin.trade.tradeinfo.TradeInfoEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class CandlePolicy implements IBuyPolicy {



    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        int buyCount = 4;
        int count = 0;

        for(int i = 1; i < buyCount + 1; i++){
            CandleStoreEntity candle = candleResponses.get(i);

            double price = candle.getTradePrice() - candle.getOpeningPrice();

            if(price < 0) {
                count++;
            }
        }

        return count >= buyCount;
    }

}
