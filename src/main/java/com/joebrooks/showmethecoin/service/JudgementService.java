package com.joebrooks.showmethecoin.service;

import org.springframework.stereotype.Service;

@Service
public class JudgementService {

    private float minPrice = 5000;

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
       float volume = minPrice / nowCoinPrice;
       if(volume * nowCoinPrice < minPrice){
           while(true){
               volume += 0.0001;

               if(volume * nowCoinPrice >= minPrice){
                    break;
               }
           }
       }
        return volume;
    }

    public boolean isProperToSell(float lastTradePrice, float nowCoinPrice){

        if(lastTradePrice < nowCoinPrice){
            return true;
        }

        return false;
    }

}
