package com.joebrooks.showmethecoin.global.strategy;

import lombok.Getter;

@Getter
public enum Strategy {
    RISING("상승장 / 모험 전략",40, 60),
    STAY("횡보장 / 일반 전략",30, 60),
    FALLING("하락장 / 안전 전략", 55, 65),
    JACKPOT("불확실장 / 잭팟 전략",20, 60);

    private int buyValue;
    private int sellValue;
    private String name;

    private Strategy(String name, int buyValue, int sellValue){
        this.name = name;
        this.buyValue = buyValue;
        this.sellValue = sellValue;
    }
}
