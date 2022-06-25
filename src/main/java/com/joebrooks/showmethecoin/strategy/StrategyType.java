package com.joebrooks.showmethecoin.strategy;

import lombok.Getter;

@Getter
public enum StrategyType {
    RsiStrategy("RSI 전략"),
    AdxDmiStrategy("Adx, Dmi 전략"),
    CandleStrategy("캔들 전략"),
    PriceStrategy("가격 전략"),
    TailStrategy("꼬리 전략"),
    BaseStrategy("기본 매매 전략"),
    RmiStrategy("RMI 전략"),
    MACDStrategy("MACD 전략"),
    QuoteStrategy("호가 전략");
    private String description;

    private StrategyType(String description){
        this.description = description;
    }
}
