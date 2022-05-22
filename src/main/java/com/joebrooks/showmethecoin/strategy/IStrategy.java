package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import com.joebrooks.showmethecoin.trade.upbit.indicator.type.IndicatorType;

import java.util.List;

public interface IStrategy {

    boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo);
    boolean isProperToSellWithBenefit(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo);
    boolean isProperToSellWithLoss(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo);
}
