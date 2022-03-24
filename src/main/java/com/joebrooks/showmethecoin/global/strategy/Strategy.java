package com.joebrooks.showmethecoin.global.strategy;

import lombok.Getter;

@Getter
public enum Strategy {
    RISING("거래량 높음 / 거래당 수익 낮음",35, 55),
    STAY("거래량 보통 / 거래당 수익 보통",30, 60),
    FALLING("거래량 낮음 / 거래당 수익 높음", 20, 60);

    private int buyValue;
    private int sellValue;
    private String name;

    private Strategy(String name, int buyValue, int sellValue){
        this.name = name;
        this.buyValue = buyValue;
        this.sellValue = sellValue;
    }
}
