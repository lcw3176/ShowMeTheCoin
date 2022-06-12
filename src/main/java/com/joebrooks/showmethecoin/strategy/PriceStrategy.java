package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.global.graph.GraphStatus;
import com.joebrooks.showmethecoin.global.graph.GraphUtil;
import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;

import java.util.List;

public class PriceStrategy implements IStrategy{

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        int fallCount = 0;

        for(int i = 1; i < 5; i++){
            GraphStatus status = GraphUtil.getStatus(candleResponses.get(i).getTradePrice(), candleResponses.get(i - 1).getTradePrice());
            if(status.equals(GraphStatus.RISING)){
                return false;
            }
        }



        return true;
    }

}
