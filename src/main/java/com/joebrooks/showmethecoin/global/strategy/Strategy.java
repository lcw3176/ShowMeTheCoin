package com.joebrooks.showmethecoin.global.strategy;

import lombok.Getter;

@Getter
public enum Strategy {
    FALLING("하락장", 30, 45),
    RISING("상승장",40, 60),
    STAY("횡보장",30, 60),
    AUTO("자동 조절", 0, 100);

    private int buyValue;
    private int sellValue;
    private String name;

    private Strategy(String name, int buyValue, int sellValue){
        this.name = name;
        this.buyValue = buyValue;
        this.sellValue = sellValue;
    }
}
