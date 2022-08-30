package com.joebrooks.showmethecoin.autotrade.strategy.policy.candle.buy;

import com.joebrooks.showmethecoin.autotrade.strategy.policy.IBuyPolicy;
import com.joebrooks.showmethecoin.repository.candle.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;

import java.util.List;


public class TailPolicy implements IBuyPolicy {

    @Override
    public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        CandleStoreEntity mostRecentCandle = candleResponses.get(0);

        // 양봉이면 종료
        if(mostRecentCandle.getTradePrice() - mostRecentCandle.getOpeningPrice() > 0){
            return false;
        }

        double tail = mostRecentCandle.getTradePrice() - mostRecentCandle.getLowPrice();
        double priceGap = mostRecentCandle.getOpeningPrice() - mostRecentCandle.getTradePrice();


        return tail / priceGap * 100 > 30;
    }

}
