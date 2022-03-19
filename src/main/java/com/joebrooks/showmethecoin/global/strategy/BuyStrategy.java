package com.joebrooks.showmethecoin.global.strategy;

public enum BuyStrategy {
    Careful(20),
    Bold(40);

    private int rsiValue;

    private BuyStrategy(int rsiValue){
        this.rsiValue = rsiValue;
    }
}
