package com.joebrooks.showmethecoin.service;

import org.springframework.beans.factory.annotation.Value;
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

    public float howMuchBuy(float nowCoinPrice){
       return minPrice / nowCoinPrice;
    }

    public boolean isProperToSell(float lastTradePrice, float nowCoinPrice){

        if(lastTradePrice < nowCoinPrice){
            return true;
        }

        return false;
    }

}
