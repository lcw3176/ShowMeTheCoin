package com.joebrooks.showmethecoin.rountine;

import com.joebrooks.showmethecoin.domain.CoinTradeInfo;
import com.joebrooks.showmethecoin.domain.DailyCoinScore;
import com.joebrooks.showmethecoin.service.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Component
@RequiredArgsConstructor
public class WatchDog {

    @Value("{upBit.minPrice}")
    private float minPrice;

    private static float lastTradePrice = 20000;
    private boolean isSellingMode = false;

    private int totalVolume = 0;
    private float firstTradePrice = 0;
    private int dealCount = 0;

    private final PriceService priceService;
    private final CheckCashService checkCashService;
    private final JudgementService judgementService;
    private final BuyService buyService;
    private final SellService sellService;

    private final MakeMessageService makeMessageService;
    private final SlackService slackService;



    @Scheduled(fixedDelay = 1000)
    public void observePrice() throws ParseException, UnsupportedEncodingException, NoSuchAlgorithmException {
        float nowPrice = priceService.getDogePrice();
        float myMoney = checkCashService.getLeftMoney();

        if(myMoney <= minPrice){
            isSellingMode = true;
        }

        if(isSellingMode){
            if(judgementService.isProperToSell(firstTradePrice, nowPrice)){
                int price = (int)nowPrice;
                sellService.Sell(totalVolume, price);

                DailyCoinScore.setSellingMoney(DailyCoinScore.getSellingMoney() + (price * totalVolume));
                totalVolume = 0;
                dealCount = 0;
                lastTradePrice = price;

                isSellingMode = false;

                myMoney = checkCashService.getLeftMoney();

                String message = makeMessageService.makeSellMessage(CoinTradeInfo.builder()
                        .price(price)
                        .volume(totalVolume)
                        .myCash(myMoney)
                        .build());

                slackService.sendMessage(message);
            }

        } else {
            if(judgementService.isProperToBuy(lastTradePrice, nowPrice, myMoney)){
                float volume = judgementService.howMuchBuy(nowPrice);
                int price = (int)nowPrice;
                buyService.Buy(volume, price);

                if(dealCount == 0){
                    firstTradePrice = price;
                }

                DailyCoinScore.setBuyingMoney(DailyCoinScore.getBuyingMoney() + (price * volume));
                totalVolume += volume;
                lastTradePrice = price;
                dealCount++;

                myMoney = checkCashService.getLeftMoney();

                String message = makeMessageService.makeBuyMessage(CoinTradeInfo.builder()
                        .price(price)
                        .volume(volume)
                        .myCash(myMoney)
                        .build());

                slackService.sendMessage(message);
            }
        }
    }
}
