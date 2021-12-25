package com.joebrooks.showmethecoin.rountine;

import com.joebrooks.showmethecoin.domain.CoinTradeInfo;
import com.joebrooks.showmethecoin.domain.DailyCoinScore;
import com.joebrooks.showmethecoin.service.CoinService;
import com.joebrooks.showmethecoin.service.MyInfoService;
import com.joebrooks.showmethecoin.service.SlackService;
import com.joebrooks.showmethecoin.util.MessageUtil;
import com.joebrooks.showmethecoin.util.OrderParseUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Queue;

@Component
@RequiredArgsConstructor
public class WatchDog {

    private float tradePrice = 20000;
    private float moderator = 2;
    private float totalVolume = 0;

    private boolean isCancelling = false;

    private final CoinService coinService;
    private final MyInfoService myInfoService;
    private final SlackService slackService;

    private Queue<String> orderQueue = new LinkedList<>();
    private Queue<String> messageQueue = new LinkedList<>();

    @Scheduled(fixedDelay = 1000)
    public void observingMachine() throws ParseException, UnsupportedEncodingException, NoSuchAlgorithmException {

        float nowPrice = coinService.getPrice("KRW-DOGE");
        float myMoney = myInfoService.getLeftMoney();

        if(isCancelling){
            return;
        }
        
        if(orderQueue.size() == 0){  // 구매시점
            if(tradePrice > nowPrice){
                float volume = myMoney / nowPrice;
                String uuid = coinService.buy(volume, nowPrice);

                totalVolume += volume;
                orderQueue.add(uuid);
                messageQueue.add(uuid);

                tradePrice = nowPrice;

                DailyCoinScore.setBuyingMoney(DailyCoinScore.getBuyingMoney() + (volume * nowPrice));
            }
        } else {      // 판매시점
            if(tradePrice + moderator < nowPrice){
                ResponseEntity<String> orderInfo = myInfoService.getOrderInfo(orderQueue.peek());



                if(OrderParseUtil.isOrderComplete(orderInfo)){
                    float sellPrice = tradePrice + moderator;

                    coinService.sell(totalVolume, sellPrice);
                    totalVolume = 0;

                    messageQueue.add(orderQueue.poll());
                    DailyCoinScore.setSellingMoney(DailyCoinScore.getSellingMoney() + (totalVolume * sellPrice));
                }
            }
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void messageMachine() throws UnsupportedEncodingException, NoSuchAlgorithmException, ParseException {
        if(messageQueue.size() > 0){
            String uuid = messageQueue.poll();
            ResponseEntity<String> orderInfo = myInfoService.getOrderInfo(uuid);

            if(OrderParseUtil.isOrderComplete(orderInfo)){
                float tradedPrice = OrderParseUtil.getPriceWhenTraded(orderInfo);
                float myMoney = myInfoService.getLeftMoney();
                float volume = OrderParseUtil.getVolumeWhenTraded(orderInfo);
                boolean wasBought = OrderParseUtil.wasBought(orderInfo);

                CoinTradeInfo coinTradeInfo = CoinTradeInfo.builder()
                        .price(tradedPrice)
                        .myCash(myMoney)
                        .volume(volume)
                        .build();

                String message;

                if(wasBought){  // 구매했을 때
                    message =  MessageUtil.makeBuyMessage(coinTradeInfo);
                } else {  // 판매했을 때
                    message =  MessageUtil.makeSellMessage(coinTradeInfo);
                }

                slackService.sendMessage(message);
            }

        }
    }

    @Scheduled(fixedDelay = 90000)
    public void adjustPriceMachine() throws UnsupportedEncodingException, NoSuchAlgorithmException, ParseException {
        if(orderQueue.size() > 0){
            isCancelling = true;

            String uuid = orderQueue.poll();
            float nowPrice = coinService.getPrice("KRW-DOGE");
            tradePrice = nowPrice;

            if(myInfoService.cancelOrder(uuid)){
                isCancelling = false;
            }
        }
    }


}
