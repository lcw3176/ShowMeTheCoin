package com.joebrooks.showmethecoin.strategy;

import lombok.Getter;

@Getter
public enum Strategy {

    RSI_STRATEGY("rsi 기반 전략");
    private String name;

    private Strategy(String name){
        this.name = name;
    }
}
