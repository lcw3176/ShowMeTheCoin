package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import com.joebrooks.showmethecoin.trade.upbit.indicator.IndicatorResponse;
import com.joebrooks.showmethecoin.trade.upbit.indicator.type.IndicatorType;

import java.util.List;

public interface IStrategy {

    boolean isProperToBuy(List<IndicatorResponse> response, List<CandleResponse> candleResponses);
    boolean isProperToSellWithBenefit(List<IndicatorResponse> response, List<CandleResponse> candleResponses);
    boolean isProperToSellWithLoss(List<IndicatorResponse> response, List<CandleResponse> candleResponses, CandleResponse lastTradeCandle);
    List<IndicatorType> getRequiredIndicators();
}
