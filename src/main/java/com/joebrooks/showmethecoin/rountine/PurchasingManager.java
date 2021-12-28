package com.joebrooks.showmethecoin.rountine;

import com.joebrooks.showmethecoin.service.CoinService;
import com.joebrooks.showmethecoin.service.MyInfoService;
import com.joebrooks.showmethecoin.util.OrderParseUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Stack;

@Component
@RequiredArgsConstructor
public class PurchasingManager {

    private float tradePrice = 2235;
    private float totalVolume = 0;
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
        ResponseEntity balanceInfo = myInfoService.getBalance();
        float availableMyMoney = OrderParseUtil.getAvailableBalance(balanceInfo, "KRW");


//        if(isCancelling){
//            return;
//        }
//
//
        if(orderCount < maxOrder && availableMyMoney >= minPrice){  // 구매시점

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

                orderStack.add(uuid);
                tradePrice = nowPrice;
                orderCount++;
            }


        } else if(orderCount >= maxOrder && nowPrice > firstTradePrice + (threshold * 2)) {      // 판매시점
            ResponseEntity<String> orderInfo = myInfoService.getOrderInfo(orderStack.peek());

            if(OrderParseUtil.isOrderComplete(orderInfo)){
                float balanceMyCoin = OrderParseUtil.getLockedBalance(balanceInfo, nowCoin.split("-")[1]);

                String uuid = coinService.sell(nowCoin, balanceMyCoin, nowPrice);
                orderStack.clear();
                tradePrice = firstTradePrice;
                orderCount = 0;

                sellStack.add(uuid);
            }
        }
    }

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
