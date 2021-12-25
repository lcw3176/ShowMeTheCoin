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

    private float tradePrice = 8130;
    private float totalVolume = 0;
//    private int timer = 0;
    private float adder = (float) 0.002;

    private boolean isCancelling = false;
    private boolean isOrdering = false;

    private final CoinService coinService;
    private final MyInfoService myInfoService;
    private final SlackService slackService;

    private final float minPrice = 5000;
    private Queue<String> orderQueue = new LinkedList<>();
    private Queue<String> messageQueue = new LinkedList<>();

    private String nowCoin = "KRW-SAND";

    @Scheduled(fixedDelay = 1000)
    public void observingMachine() throws ParseException, UnsupportedEncodingException, NoSuchAlgorithmException {

        float nowPrice = coinService.getPrice(nowCoin);
        float myMoney = myInfoService.getLeftMoney();
        isOrdering = true;
        float adjust = nowPrice * adder;

        if(isCancelling){
            return;
        }

        if(orderQueue.size() == 0 && myMoney > minPrice){  // 구매시점
            if(Math.ceil(tradePrice - adjust) - Math.ceil(tradePrice - adjust) % 10 >= nowPrice){
                float volume = (float)(Math.ceil(myMoney / nowPrice * 100000) / 100000);
                volume *= 0.96;

                String uuid = coinService.buy(nowCoin, volume, nowPrice);

                totalVolume += volume;
                orderQueue.add(uuid);
                messageQueue.add(uuid);

                tradePrice = nowPrice;

                DailyCoinScore.setBuyingMoney(DailyCoinScore.getBuyingMoney() + (volume * nowPrice));
            }
        } else if(orderQueue.size() > 0) {      // 판매시점
            if(Math.ceil(tradePrice + adjust) - Math.ceil(tradePrice + adjust) % 10 < nowPrice){
                ResponseEntity<String> orderInfo = myInfoService.getOrderInfo(orderQueue.peek());

                if(OrderParseUtil.isOrderComplete(orderInfo)){
                    coinService.sell(nowCoin, totalVolume, nowPrice);
                    totalVolume = 0;

                    messageQueue.add(orderQueue.poll());
                    DailyCoinScore.setSellingMoney(DailyCoinScore.getSellingMoney() + (totalVolume * nowPrice));

                    tradePrice = nowPrice;
//                    timer = 0;
                }
            }
        }

        isOrdering = false;
//        timer++;
    }

    @Scheduled(fixedDelay = 5000)
    public void messageMachine() throws UnsupportedEncodingException, NoSuchAlgorithmException, ParseException {

        if(isOrdering){
            return;
        }

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

//    @Scheduled(fixedDelay = 360000)
//    public void adjustPriceMachine() throws UnsupportedEncodingException, NoSuchAlgorithmException, ParseException {
//        if(orderQueue.size() > 0 && timer > 1800){
//            isCancelling = true;
//
//            String uuid = orderQueue.poll();
//            float nowPrice = coinService.getPrice(nowCoin);
//            tradePrice = nowPrice;
//
//            if(myInfoService.cancelOrder(uuid)){
//                isCancelling = false;
//                timer = 0;
//            }
//        }
//    }


}
