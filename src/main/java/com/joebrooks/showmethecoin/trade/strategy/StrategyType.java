package com.joebrooks.showmethecoin.trade.strategy;

import com.joebrooks.showmethecoin.trade.strategy.type.BaseStrategy;
import com.joebrooks.showmethecoin.trade.strategy.type.ChaserStrategy;
import com.joebrooks.showmethecoin.trade.strategy.type.ShortStrategy;
import lombok.Getter;

@Getter
public enum StrategyType {
    BASE("기본적인 매매 전략, 자동으로 포함됨", BaseStrategy.class),
    CHASER("추격 매매 전략", ChaserStrategy.class),
    SHORT("단타 전략", ShortStrategy.class);
    private String description;
    private Class clazz;

    private StrategyType(String description, Class clazz){
        this.description = description;
        this.clazz = clazz;
    }
}
