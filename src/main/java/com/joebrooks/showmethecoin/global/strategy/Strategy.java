package com.joebrooks.showmethecoin.global.strategy;

public enum Strategy {
    Careful(20, 40),
    Bold(40, 60),
    RealMan(20, 60);

    private int buyValue;
    private int sellValue;

    private Strategy(int buyValue, int sellValue){
        this.buyValue = buyValue;
        this.sellValue = sellValue;
    }
}
