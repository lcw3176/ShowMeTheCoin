package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;

import java.util.List;


public class TailStrategy implements IStrategy{

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        CandleResponse mostRecentCandle = candleResponses.get(0);

        // 양봉이면 종료
        if(mostRecentCandle.getTradePrice() - mostRecentCandle.getOpeningPrice() > 0){
            return false;
        }

        // 마지막 거래와 같은 캔들(같은 시각)이면 종료
        if(!tradeInfo.isEmpty() && tradeInfo.get(tradeInfo.size() - 1).getDateKst().equals(mostRecentCandle.getDateKst())){
            return false;
        }

        double tail = mostRecentCandle.getTradePrice() - mostRecentCandle.getLowPrice();
        double priceGap = mostRecentCandle.getOpeningPrice() - mostRecentCandle.getTradePrice();


        return tail / priceGap * 100 > 30;
    }

}
