package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;

import java.util.List;

public class WStrategy implements IStrategy{



    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        int count = 0;
        double percentage = 0.05;
        double minValue = Double.POSITIVE_INFINITY;
        double maxValue = Double.NEGATIVE_INFINITY;

        for(int i = 0; i < 10; i++){
            CandleResponse temp = candleResponses.get(i);
            minValue = Math.min(temp.getTradePrice(), minValue);
            maxValue = Math.max(temp.getTradePrice(), maxValue);
        }

        for(int i = 0; i < 10; i++){
            CandleResponse temp = candleResponses.get(i);


            if(Math.min(minValue, temp.getTradePrice()) / Math.max(minValue, temp.getTradePrice()) * 100 > (100 - percentage)){
                count++;
            }



        }

        return count >= 3
                && minValue / maxValue * 100 < 100 - 2
                && Math.min(minValue, candleResponses.get(0).getTradePrice()) / Math.max(minValue, candleResponses.get(0).getTradePrice()) * 100 > (100 - percentage);
    }

//    @Override
//    public boolean isProperToSellWithBenefit(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
//
//        double maxValue = Double.NEGATIVE_INFINITY;
//        double percentage = 0.0005;
//        for(int i = 0; i < 100; i++){
//            CandleResponse temp = candleResponses.get(i);
//            maxValue = Math.max(temp.getTradePrice(), maxValue);
//        }
//
//        return candleResponses.get(0).getTradePrice() * (1 + percentage) < maxValue;
//    }


//    @Override
//    public boolean isProperToSellWithLoss(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
//
//        double minValue = Double.NEGATIVE_INFINITY;
//        double percentage = 0.005;
//        for(int i = 0; i < 100; i++){
//            CandleResponse temp = candleResponses.get(i);
//            minValue = Math.min(temp.getTradePrice(), minValue);
//        }
//
//        return candleResponses.get(0).getTradePrice() * (1 - percentage) > minValue;
//    }
}
