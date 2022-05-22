package com.joebrooks.showmethecoin.strategy;

import lombok.Getter;

@Getter
public enum Strategy {

    PRICE_STRATEGY("가격 전략"),
    TAIL_STRATEGY("꼬리 전략"),
    QUOTE_STRATEGY("호가 전략");
    private String description;

    private Strategy(String description){
        this.description = description;
    }
}
