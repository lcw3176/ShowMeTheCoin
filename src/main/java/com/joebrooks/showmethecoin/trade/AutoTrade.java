package com.joebrooks.showmethecoin.trade;

import com.joebrooks.showmethecoin.global.exception.type.AutomationException;
import com.joebrooks.showmethecoin.repository.userConfig.UserConfigService;
import com.joebrooks.showmethecoin.strategy.IStrategy;
import com.joebrooks.showmethecoin.strategy.StrategyService;
import com.joebrooks.showmethecoin.strategy.StrategyType;
import com.joebrooks.showmethecoin.trade.upbit.account.AccountResponse;
import com.joebrooks.showmethecoin.trade.upbit.account.AccountService;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleService;
import com.joebrooks.showmethecoin.trade.upbit.client.CoinType;
import com.joebrooks.showmethecoin.trade.upbit.client.OrderType;
import com.joebrooks.showmethecoin.trade.upbit.client.Side;
import com.joebrooks.showmethecoin.trade.upbit.order.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoTrade {

    private final AccountService accountService;
    private final OrderService orderService;
    private final CandleService candleService;
    private final UserConfigService userConfigService;
    private final List<TradeInfo> tradeInfoList = new LinkedList<>();
    private final List<IStrategy> strategy = new LinkedList<>();
    private final StrategyService strategyService;
    private volatile double minCash;
    private volatile boolean wasSold = false;
    private LocalDateTime lastOrderTime = null;

    @PostConstruct
    public void init(){
        List<StrategyType> strategyTypeList = new LinkedList<>();
        strategyTypeList.add(StrategyType.RsiStrategy);
        strategyTypeList.add(StrategyType.RmiStrategy);
        strategyTypeList.add(StrategyType.BaseStrategy);

        for(StrategyType i : strategyTypeList){
            strategy.add(strategyService.get(i));
        }
        initMinCash();
    }


    private void initMinCash(){
        int divideNum = 7;

        AccountResponse accountResponse = accountService.getKRWCurrency();
        double myBalance = Math.ceil(Double.parseDouble(accountResponse.getBalance()));

        minCash = Math.ceil(myBalance / divideNum * 0.99);
    }

    private boolean isOrderDone(){
        return orderService.checkOrder(CheckOrderRequest.builder()
                .state(OrderStatus.wait)
                .build()).isEmpty();
    }

    @Scheduled(fixedDelay = 3000)
    public void autoTrade() throws AutomationException {

        try {
            userConfigService.getAllUserConfig().forEach(user -> {
                if (!user.isTrading()) {
                    return;
                }

                // 주문 완료 처리
                if(wasSold && isOrderDone()){
                    tradeInfoList.clear();
                    wasSold = false;
                    initMinCash();
                }

                // 주문 취소 처리
                if(lastOrderTime != null && lastOrderTime.plusMinutes(1).isBefore(LocalDateTime.now())){
                    wasSold = false;

                    List<CheckOrderResponse> checkOrderResponseList = orderService.checkOrder(CheckOrderRequest.builder()
                            .state(OrderStatus.wait)
                            .build());

                    for(CheckOrderResponse order : checkOrderResponseList){
                        orderService.cancelOrder(CancelOrderRequest.builder()
                                .uuid(order.getUuid())
                                .build());

                        tradeInfoList.removeIf(info -> info.getUuid().equals(order.getUuid()));
                    }
                }


                CoinType coinType = user.getTradeCoin();
                List<CandleResponse> candles = candleService.getCandles(coinType);

                CandleResponse nowCandle = candles.get(0);
                CandleResponse beforeCandle = candles.get(1);

                // 구매
                if ((tradeInfoList.isEmpty() || !nowCandle.getDateKst().equals(tradeInfoList.get(tradeInfoList.size() - 1).getDateKst()))
                        && !wasSold
                        && strategy.stream().allMatch(st -> st.isProperToBuy(candles, tradeInfoList))) {
                    AccountResponse accountResponse = accountService.getKRWCurrency();


                    double myBalance = Math.ceil(Double.parseDouble(accountResponse.getBalance()));
                    // 잔고 체크
                    if (myBalance > minCash) {
                        double coinVolume = minCash / beforeCandle.getTradePrice();

                        OrderRequest orderRequest = OrderRequest.builder()
                                .market(coinType.getName())
                                .side(Side.bid)
                                .price(Double.toString(beforeCandle.getTradePrice()))
                                .volume(BigDecimal.valueOf(coinVolume).toString())
                                .ordType(OrderType.limit)
                                .build();

                        OrderResponse orderResponse = orderService.requestOrder(orderRequest);

                        TradeInfo tradeInfo = TradeInfo.builder()
                                .tradePrice(beforeCandle.getTradePrice())
                                .coinVolume(coinVolume)
                                .dateKst(nowCandle.getDateKst())
                                .uuid(orderResponse.getUuid())
                                .build();

                        tradeInfoList.add(tradeInfo);
                        lastOrderTime = LocalDateTime.now();
                    }

                }

                // 익절
                else if (!tradeInfoList.isEmpty()
                        && strategy.stream().allMatch(st -> st.isProperToSellWithBenefit(candles, tradeInfoList))) {

                    AccountResponse coinResponse = accountService.getCoinCurrency(coinType);
                    double coinBalance = Double.parseDouble(coinResponse.getBalance());

                    if (coinBalance > 0) {
                        OrderRequest orderRequest = OrderRequest.builder()
                                .market(coinType.getName())
                                .side(Side.ask)
                                .price(Double.toString(beforeCandle.getTradePrice()))
                                .volume(BigDecimal.valueOf(coinBalance).toString())
                                .ordType(OrderType.limit)
                                .build();

                        orderService.requestOrder(orderRequest);
                        wasSold = true;
                        lastOrderTime = LocalDateTime.now();
                    }
                }

                // 손절
                else if (!tradeInfoList.isEmpty()
                        && !nowCandle.getDateKst().equals(tradeInfoList.get(tradeInfoList.size() - 1).getDateKst())
                        && strategy.stream().allMatch(st -> st.isProperToSellWithLoss(candles, tradeInfoList))) {

                        AccountResponse coinResponse = accountService.getCoinCurrency(coinType);
                        double coinBalance = Double.parseDouble(coinResponse.getBalance());

                    if (coinBalance > 0) {
                        OrderRequest orderRequest = OrderRequest.builder()
                                .market(coinType.getName())
                                .side(Side.ask)
                                .price(Double.toString(beforeCandle.getTradePrice()))
                                .volume(BigDecimal.valueOf(coinBalance).toString())
                                .ordType(OrderType.limit)
                                .build();

                        orderService.requestOrder(orderRequest);
                        wasSold = true;
                        lastOrderTime = LocalDateTime.now();
                    }
                }
            });
        } catch (Exception e) {
            throw new AutomationException(e, "에러");
        }

    }

}
