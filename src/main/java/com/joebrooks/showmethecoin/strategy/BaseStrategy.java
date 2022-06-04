package com.joebrooks.showmethecoin.strategy;


import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;

import java.util.List;

public class BaseStrategy implements IStrategy {

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        if(tradeInfo.size() > 0){
            return tradeInfo.get(tradeInfo.size() - 1).getTradePrice() > candleResponses.get(0).getTradePrice();
        }

        return true;
    }

}