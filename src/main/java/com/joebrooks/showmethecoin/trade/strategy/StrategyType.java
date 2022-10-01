package com.joebrooks.showmethecoin.trade.strategy;

import com.joebrooks.showmethecoin.repository.candlestore.CandleMinute;
import com.joebrooks.showmethecoin.trade.strategy.type.BaseStrategy;
import com.joebrooks.showmethecoin.trade.strategy.type.ChaserStrategy;
import com.joebrooks.showmethecoin.trade.strategy.type.ShortStrategy;
import lombok.Getter;

@Getter
public enum StrategyType {
    BASE("기본적인 매매 전략, 자동으로 포함됨", BaseStrategy.class, null),
    CHASER("추격 매매 전략", ChaserStrategy.class, CandleMinute.ONE_M),
    SHORT("단타 전략", ShortStrategy.class, CandleMinute.ONE_M);
    private String description;
    private Class clazz;
    private CandleMinute candleMinute;

    private StrategyType(String description, Class clazz, CandleMinute candleMinute){
        this.description = description;
        this.clazz = clazz;
        this.candleMinute = candleMinute;
    }
}
