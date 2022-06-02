package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;

import java.util.List;

public interface IStrategy {
    double lossRate = 0.1;
    double gainRate = 0.05;

    boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo);
    default boolean isProperToSellWithBenefit(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo){
        double averagePrice = getAveragePrice(tradeInfo);

        return (averagePrice * 1.0005) * 1.0005 < candleResponses.get(0).getTradePrice(); // 실 매도시 인덱스 +1;
    }

    default boolean isProperToSellWithLoss(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo){
        double averagePrice = getAveragePrice(tradeInfo);

        return (averagePrice * 1.0005) * (1 - lossRate) > candleResponses.get(0).getTradePrice(); // 실 매도시 인덱스 +1
    }

    default double getAveragePrice(List<TradeInfo> tradeInfo){
        double price = 0;
        double volume = 0;

        for(int i = 0; i < tradeInfo.size(); i++){
            price += tradeInfo.get(i).getTradePrice() * tradeInfo.get(i).getCoinVolume() * 1.0005;
            volume += tradeInfo.get(i).getCoinVolume();
        }

        return price / volume;
    }
}
