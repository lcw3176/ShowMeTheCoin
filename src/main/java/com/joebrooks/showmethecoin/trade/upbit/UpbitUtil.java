package com.joebrooks.showmethecoin.trade.upbit;

import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class UpbitUtil {


    public double getTickSize(double price){
        double tickSize = 0D;

        if(price >= 2000000){
            tickSize = 1000;
        } else if(price >= 1000000) {
            tickSize =  500;
        } else if(price >= 500000){
            tickSize = 100;
        } else if(price >= 100000){
            tickSize = 50;
        } else if(price >= 10000){
            tickSize = 10;
        } else if(price >= 1000){
            tickSize = 5;
        } else if(price >= 100){
            tickSize = 1;
        } else if(price >= 10){
            tickSize = 0.1;
        } else if(price >= 1){
            tickSize = 0.01;
        } else if(price >= 0.1){
            tickSize = 0.001;
        } else {
            tickSize = 0.0001;
        }

        return tickSize;
    }


    public void delay(int milliseconds){
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException("업비트 쓰레드 딜레이 에러", e);
        }
    }
}
