package com.joebrooks.showmethecoin.global.fee;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FeeCalculator {

    public double calculate(double tradePrice, double coinVolume){
        return tradePrice * coinVolume * 0.0005;
    }
}
