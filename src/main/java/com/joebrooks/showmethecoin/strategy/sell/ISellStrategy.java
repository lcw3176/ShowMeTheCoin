package com.joebrooks.showmethecoin.strategy.sell;

import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;

import java.util.List;

public interface ISellStrategy {

    boolean isProperToSellWithBenefit(List<CandleResponse> candleResponses, List<TradeInfoEntity> tradeInfo);

    boolean isProperToSellWithLoss(List<CandleResponse> candleResponses, List<TradeInfoEntity> tradeInfo);
}
