package com.joebrooks.showmethecoin.strategy;

import lombok.Getter;

@Getter
public enum StrategyType {
    RsiStrategy("RSI 기반 전략"),
    AdxDmiStrategy("Adx, Dmi 기반 전략"),
    PriceStrategy("가격 전략"),
    TailStrategy("꼬리 전략"),
    BaseStrategy("기본 매매 전략"),
    QuoteStrategy("호가 전략");
    private String description;

    private StrategyType(String description){
        this.description = description;
    }
}
