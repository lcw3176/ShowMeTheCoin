package com.joebrooks.showmethecoin.strategy.buy;

import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;

import java.util.List;

public interface IBuyStrategy {

    boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfoEntity> tradeInfo);
}
