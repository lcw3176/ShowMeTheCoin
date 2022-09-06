package com.joebrooks.showmethecoin.presentation.userconfig;

import com.joebrooks.showmethecoin.trade.strategy.StrategyType;
import com.joebrooks.showmethecoin.repository.candle.CandleMinute;
import lombok.Data;

@Data
public class UserConfigRequest {
    private StrategyType strategyType;
    private int maxTradeCoinCount;
    private int maxBetCount;
    private int orderCancelMinute;
    private CandleMinute candleMinute;
    private boolean allowSellWithLoss;
    private int cashDividedCount;
}
