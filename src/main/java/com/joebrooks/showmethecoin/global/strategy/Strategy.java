package com.joebrooks.showmethecoin.global.strategy;

import lombok.Getter;

@Getter
public enum Strategy {
    FALLING("하락장", 25, 45),
    RISING("상승장",40, 60),
    STAY("횡보장",30, 60);

    private int buyValue;
    private int sellValue;
    private String name;

    private Strategy(String name, int buyValue, int sellValue){
        this.name = name;
        this.buyValue = buyValue;
        this.sellValue = sellValue;
    }
}
