package com.joebrooks.showmethecoin.trade.strategy.policy;

import com.joebrooks.showmethecoin.repository.candle.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;

import java.util.List;

public interface IBuyPolicy {

    boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo);
}
