package com.joebrooks.showmethecoin.strategy;

import lombok.Getter;

@Getter
public enum Strategy {

    PRICE_STRATEGY("가격 기반 전략"),
    RSI_STRATEGY("rsi 기반 전략");
    private String description;

    private Strategy(String description){
        this.description = description;
    }
}
