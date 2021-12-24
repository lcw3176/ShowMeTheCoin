package com.joebrooks.showmethecoin.service;

import org.springframework.stereotype.Service;

@Service
public class JudgementService {

    private float minPrice = 5400;

    public boolean isProperToBuy(float lastTradePrice, float nowCoinPrice, float myMoney){

        if (myMoney < minPrice){
            return false;
        }

        if(lastTradePrice > nowCoinPrice){
            return true;
        }

        return false;
    }

    public int howMuchBuy(float nowCoinPrice){
        return (int) Math.floor(minPrice / nowCoinPrice);
    }

    public boolean isProperToSell(float lastTradePrice, float nowCoinPrice){

        if(lastTradePrice < nowCoinPrice){
            return true;
        }

        return false;
    }

}
