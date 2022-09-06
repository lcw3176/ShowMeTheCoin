package com.joebrooks.showmethecoin.trade.strategy.policy.base;


import com.joebrooks.showmethecoin.trade.candle.CandleStoreEntity;
import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import com.joebrooks.showmethecoin.trade.tradeinfo.TradeInfoEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaseBuyCore implements IBuyPolicy {

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        if(!tradeInfo.isEmpty()){
            return tradeInfo.get(tradeInfo.size() - 1).getTradePrice() > candleResponses.get(1).getTradePrice();
        }

        return true;
    }
}
