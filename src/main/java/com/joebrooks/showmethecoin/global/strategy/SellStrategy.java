package com.joebrooks.showmethecoin.global.strategy;

public enum SellStrategy {
    Careful(50),
    Bold(60);

    private int rsiValue;

    private SellStrategy(int rsiValue){
        this.rsiValue = rsiValue;
    }
}
