package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.global.fee.FeeCalculator;
import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;

import java.util.List;


public interface IStrategy {
    double lossRate = 0.01;
    double gainRate = 0.0005;

    boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo);
    default boolean isProperToSellWithBenefit(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo){
        double averageBuyPrice = getAverageBuyPrice(tradeInfo);
        double paidFee = getPaidFee(tradeInfo);
        double averageSellPrice = getAverageSellPrice(candleResponses, tradeInfo);
        double payingFee = getPayingFee(candleResponses, tradeInfo);

        return (averageBuyPrice + paidFee + payingFee) < averageSellPrice; // 실 매도시 인덱스 +1;
    }

    default boolean isProperToSellWithLoss(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo){
        double averageBuyPrice = getAverageBuyPrice(tradeInfo);
        double paidFee = getPaidFee(tradeInfo);
        double averageSellPrice = getAverageSellPrice(candleResponses, tradeInfo);
        double payingFee = getPayingFee(candleResponses, tradeInfo);

        return (averageBuyPrice + paidFee + payingFee) * (1 - lossRate) > averageSellPrice; // 실 매도시 인덱스 +1
    }

    default double getAverageBuyPrice(List<TradeInfo> tradeInfo){
        double price = 0;

        for(TradeInfo i : tradeInfo){
            price += i.getTradePrice() * i.getCoinVolume();
        }

        return price;
    }

    default double getPaidFee(List<TradeInfo> tradeInfo){
        double fee = 0;

        for(TradeInfo i : tradeInfo){
            fee += FeeCalculator.calculate(i.getTradePrice() * i.getCoinVolume(), i.getCoinVolume());
        }

        return fee;
    }

    default double getAverageSellPrice(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo){
        double volume = 0;

        for(TradeInfo i : tradeInfo){
            volume += i.getCoinVolume();
        }

        return volume * candleResponses.get(0).getTradePrice();
    }

    default double getPayingFee(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo){
        double volume = 0;

        for(TradeInfo i : tradeInfo){
            volume += i.getCoinVolume();
        }

        return FeeCalculator.calculate(candleResponses.get(0).getTradePrice() * volume, volume);
    }
}
