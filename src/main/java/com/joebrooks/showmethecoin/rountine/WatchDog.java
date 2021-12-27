package com.joebrooks.showmethecoin.rountine;

import com.joebrooks.showmethecoin.domain.DailyCoinScore;
import com.joebrooks.showmethecoin.service.CoinService;
import com.joebrooks.showmethecoin.service.MyInfoService;
import com.joebrooks.showmethecoin.service.SlackService;
import com.joebrooks.showmethecoin.util.OrderParseUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

@Component
@RequiredArgsConstructor
public class WatchDog {

    private float tradePrice = 2235;
    private float totalVolume = 0;
//    private int timer = 0;
//    private float adder = (float) 0.005;
    private int maxOrder = 3;
    private int orderCount = 0;
    private float firstTradePrice = 0;
    private final float threshold = 5;

    private boolean isCancelling = false;
//    private boolean isOrdering = false;

    private final CoinService coinService;
    private final MyInfoService myInfoService;
//    private final SlackService slackService;

    private final float minPrice = 9500;
    private Stack<String> orderStack = new Stack<>();
    private Stack<String> sellStack = new Stack<>();
//    private Queue<String> messageQueue = new LinkedList<>();

    private String nowCoin = "KRW-HIVE";

//    @PostConstruct
//    public void setTradePrice() throws ParseException {
//        tradePrice = coinService.getPrice(nowCoin);
//    }


//    @Scheduled(fixedDelay = 600000)
//    public void resetTradePrice() throws ParseException {
//        float nowPrice = coinService.getPrice(nowCoin);
//
//        if(Math.abs(tradePrice - nowPrice)  > 20 && orderCount == 0){
//            tradePrice = nowPrice;
//        }
//    }

    @Scheduled(fixedDelay = 1000)
    public void observingMachine() throws ParseException, UnsupportedEncodingException, NoSuchAlgorithmException {

        float nowPrice = coinService.getPrice(nowCoin);
        float myMoney = myInfoService.getLeftMoney();
//        isOrdering = true;
//        float adjust = nowPrice * adder;
//        float adjustedSellPrice = (float)(Math.ceil(tradePrice + adjust) - Math.ceil(tradePrice + adjust) % 10);

        if(isCancelling){
            return;
        }


        if(orderCount < maxOrder && myMoney >= minPrice){  // 구매시점
//            float adjustedBuyPrice = (float) (Math.ceil(tradePrice - adjust) - Math.ceil(tradePrice - adjust) % 10);

            if(sellStack.size() > 0){
                ResponseEntity<String> orderInfo = myInfoService.getOrderInfo(sellStack.peek());

                if(OrderParseUtil.isOrderComplete(orderInfo)){
                    sellStack.clear();
                } else {
                    return;
                }
            }

            if(tradePrice - threshold >= nowPrice){
                float volume = (float)(Math.ceil(minPrice / nowPrice * 100000) / 100000);

                String uuid = coinService.buy(nowCoin, volume, nowPrice);

                if(orderCount == 0){
                    firstTradePrice = nowPrice;
                }

                totalVolume += volume;
                orderStack.add(uuid);
//                messageQueue.add(uuid);

                tradePrice = nowPrice;

                DailyCoinScore.setBuyingMoney(DailyCoinScore.getBuyingMoney() + (volume * nowPrice));

                orderCount++;
            }


        } else if(orderCount >= maxOrder && nowPrice > firstTradePrice + threshold) {      // 판매시점
            ResponseEntity<String> orderInfo = myInfoService.getOrderInfo(orderStack.peek());

            if(OrderParseUtil.isOrderComplete(orderInfo)){
                String uuid = coinService.sell(nowCoin, totalVolume, nowPrice);
                totalVolume = 0;
                orderStack.clear();
//                    messageQueue.add(orderQueue.poll());
                DailyCoinScore.setSellingMoney(DailyCoinScore.getSellingMoney() + (totalVolume * nowPrice));

                tradePrice = firstTradePrice;
//                    tradePrice = nowPrice;
//                    timer = 0;
                orderCount = 0;

                sellStack.add(uuid);
            }
        }

//        isOrdering = false;
//        timer++;
    }
//
//    @Scheduled(fixedDelay = 5000)
//    public void messageMachine() throws UnsupportedEncodingException, NoSuchAlgorithmException, ParseException {
//
//        if(isOrdering){
//            return;
//        }
//
//        if(messageQueue.size() > 0){
//            String uuid = messageQueue.poll();
//            ResponseEntity<String> orderInfo = myInfoService.getOrderInfo(uuid);
//
//            if(OrderParseUtil.isOrderComplete(orderInfo)){
//                float tradedPrice = OrderParseUtil.getPriceWhenTraded(orderInfo);
//                float myMoney = myInfoService.getLeftMoney();
//                float volume = OrderParseUtil.getVolumeWhenTraded(orderInfo);
//                boolean wasBought = OrderParseUtil.wasBought(orderInfo);
//
//                CoinTradeInfo coinTradeInfo = CoinTradeInfo.builder()
//                        .price(tradedPrice)
//                        .myCash(myMoney)
//                        .volume(volume)
//                        .build();
//
//                String message;
//
//                if(wasBought){  // 구매했을 때
//                    message =  MessageUtil.makeBuyMessage(coinTradeInfo);
//                } else {  // 판매했을 때
//                    message =  MessageUtil.makeSellMessage(coinTradeInfo);
//                }
//
//                slackService.sendMessage(message);
//            }
//
//        }
//    }

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
