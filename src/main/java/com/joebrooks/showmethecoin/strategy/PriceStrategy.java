package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;

import java.util.List;

public class PriceStrategy implements IStrategy{

    private final double percentage = 2;

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        int count = 0;

        double minValue = 1000000000;

        for(CandleResponse i : candleResponses){
            minValue = Math.min(i.getTradePrice(), minValue);
        }


        for(int i = 0; i < 10; i++){
            CandleResponse temp = candleResponses.get(i);
            if(Math.min(minValue, temp.getTradePrice()) / Math.max(minValue, temp.getTradePrice()) * 100 > (100 - percentage)){
                count++;
            }

        }

        return count >= 2;
    }

}
