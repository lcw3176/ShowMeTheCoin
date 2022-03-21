package com.joebrooks.showmethecoin.global.routine;

import com.joebrooks.showmethecoin.global.exception.type.AutomationException;
import com.joebrooks.showmethecoin.global.strategy.Strategy;
import com.joebrooks.showmethecoin.repository.userConfig.UserConfigService;
import com.joebrooks.showmethecoin.upbit.account.AccountResponse;
import com.joebrooks.showmethecoin.upbit.account.AccountService;
import com.joebrooks.showmethecoin.upbit.candles.CandleResponse;
import com.joebrooks.showmethecoin.upbit.candles.CandleService;
import com.joebrooks.showmethecoin.upbit.client.CoinType;
import com.joebrooks.showmethecoin.upbit.client.OrderType;
import com.joebrooks.showmethecoin.upbit.client.Side;
import com.joebrooks.showmethecoin.upbit.indicator.IndicatorResponse;
import com.joebrooks.showmethecoin.upbit.indicator.IndicatorService;
import com.joebrooks.showmethecoin.upbit.indicator.type.IndicatorType;
import com.joebrooks.showmethecoin.upbit.order.*;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoTrade {

    private final IndicatorService indicatorService;
    private final AccountService accountService;
    private final OrderService orderService;
    private final CandleService candleService;
    private final UserConfigService userConfigService;

    private boolean isAvailable = true;

    private final double initValue = 100000000D;
    private double lastTradePrice = initValue;
    private CandleResponse lastTradeCandle = null;
    private final int averageCount = 14;
    private final int offset = 6;
    private final IndicatorType rsiIndicator = IndicatorType.RSI;

    @Scheduled(fixedDelay = 3000)
    public void mainAutomationRoutine() throws AutomationException {

        try {

            if(!isAvailable){
                return;
            }


            userConfigService.getAllUserConfig().forEach(user -> {
                if(!user.isTrading()){
                    return;
                }

                CoinType coinType = user.getTradeCoin();
                double startPrice = user.getStartPrice();
                int nowLevel = user.getDifferenceLevel();
                double commonDifference = user.getCommonDifference();

                double minCash = startPrice + commonDifference * nowLevel;

                List<CandleResponse> candles = candleService.getCandles(coinType);
                IndicatorResponse rsi = indicatorService.execute(rsiIndicator, candles);

                double mostRecentValue = rsi.getValues().get(0);
                double secondRecentValue = rsi.getValues().get(1);
                double thirdRecentValue = rsi.getValues().get(2);

                CandleResponse nowCandle = candles.get(0);

                int buy;
                int sell;

                if(user.getStrategy().equals(Strategy.AUTO)){
                    double temp = 0;
                    for(int i = 0; i < averageCount; i++){
                        temp += rsi.getValues().get(i);
                    }

                    double averageRsi = temp / averageCount;

                    buy = (int)averageRsi - offset;
                    sell = (int)averageRsi + offset;

                } else {
                    buy = user.getStrategy().getBuyValue();
                    sell = user.getStrategy().getSellValue();
                }

                if(secondRecentValue > buy
                        && thirdRecentValue < buy
                        && (mostRecentValue > buy && mostRecentValue < sell - offset)
                        && (lastTradeCandle == null || !nowCandle.getDateKst().equals(lastTradeCandle.getDateKst())) ){

                    AccountResponse accountResponse = Arrays.stream(accountService.getAccountData())
                            .filter(data -> data.getCurrency().equals("KRW"))
                            .findFirst()
                            .orElseThrow(() ->{
                                throw new RuntimeException("계좌 정보가 없습니다");
                            });

                    double myBalance = Math.ceil(Double.parseDouble(accountResponse.getBalance())) ;


                    if(myBalance > minCash && nowCandle.getTradePrice() < lastTradePrice){
                        double coinVolume = minCash / nowCandle.getTradePrice();

                        OrderRequest orderRequest = OrderRequest.builder()
                                .market(coinType.getName())
                                .side(Side.bid)
                                .price(Double.toString(nowCandle.getTradePrice()))
                                .volume(BigDecimal.valueOf(coinVolume).toString())
                                .ordType(OrderType.limit)
                                .build();

                        orderService.requestOrder(orderRequest);

                        log.info("{}: 매수 {}", coinType.getKoreanName(), nowCandle.getTradePrice());

                        lastTradePrice = nowCandle.getTradePrice();
                        lastTradeCandle = nowCandle;
                        user.changeDifferenceLevel(user.getDifferenceLevel() + 1);
                        userConfigService.save(user);
                    }

                }

                if(mostRecentValue >= sell){

                    AccountResponse coinResponse = Arrays.stream(accountService.getAccountData())
                            .filter(data -> data.getCurrency().equals(coinType.toString()))
                            .findFirst()
                            .orElse(AccountResponse.builder().balance("0").build());

                    double coinBalance = Double.parseDouble(coinResponse.getBalance());

                    if(coinBalance > 0){
                        OrderRequest orderRequest = OrderRequest.builder()
                                .market(coinType.getName())
                                .side(Side.ask)
                                .price(Double.toString(nowCandle.getTradePrice()))
                                .volume(BigDecimal.valueOf(coinBalance).toString())
                                .ordType(OrderType.limit)
                                .build();

                        orderService.requestOrder(orderRequest);

                        log.info("{}: 매도 {}", coinType.getKoreanName(), nowCandle.getTradePrice());
                        lastTradePrice = initValue;
                        user.changeDifferenceLevel(0);
                        userConfigService.save(user);

//                        List<CheckOrderResponse> checkOrderResponses = orderService.checkOrder(CheckOrderRequest.builder()
//                                                    .state(OrderStatus.wait)
//                                                    .build());
//
//
//                        checkOrderResponses.forEach(remainedOrder -> {
//                            orderService.cancelOrder(CancelOrderRequest.builder()
//                                    .uuid(remainedOrder.getUuid())
//                                    .build());
//                        });
                    }
                }

            });



        } catch (ReadTimeoutException e){
            log.info("타임아웃");
        } catch (Exception e){
            isAvailable = false;
            throw new AutomationException(e, "작동 정지");
        }
    }

}
