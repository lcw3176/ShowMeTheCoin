package com.joebrooks.showmethecoin.trade;

import com.joebrooks.showmethecoin.global.exception.type.AutomationException;
import com.joebrooks.showmethecoin.global.fee.FeeCalculator;
import com.joebrooks.showmethecoin.repository.userConfig.UserConfigService;
import com.joebrooks.showmethecoin.strategy.IStrategy;
import com.joebrooks.showmethecoin.strategy.Strategy;
import com.joebrooks.showmethecoin.strategy.StrategyService;
import com.joebrooks.showmethecoin.trade.upbit.account.AccountResponse;
import com.joebrooks.showmethecoin.trade.upbit.account.AccountService;
import com.joebrooks.showmethecoin.trade.upbit.backtest.BackTestResponse;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleService;
import com.joebrooks.showmethecoin.trade.upbit.client.CoinType;
import com.joebrooks.showmethecoin.trade.upbit.client.OrderType;
import com.joebrooks.showmethecoin.trade.upbit.client.Side;
import com.joebrooks.showmethecoin.trade.upbit.indicator.IndicatorResponse;
import com.joebrooks.showmethecoin.trade.upbit.indicator.IndicatorService;
import com.joebrooks.showmethecoin.trade.upbit.indicator.type.IndicatorType;
import com.joebrooks.showmethecoin.trade.upbit.order.OrderRequest;
import com.joebrooks.showmethecoin.trade.upbit.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoTrade {

    private final AccountService accountService;
    private final OrderService orderService;
    private final CandleService candleService;
    private final UserConfigService userConfigService;

    private final List<TradeInfo> tradeInfoList = new LinkedList<>();
    private final List<Strategy> strategyList = new LinkedList<>();
    private final List<IStrategy> strategy = new LinkedList<>();
    private final StrategyService strategyService;

    private double coinBalance = 0D;

    @PostConstruct
    public void init(){
        strategyList.add(Strategy.PRICE_STRATEGY);
        strategyList.add(Strategy.TAIL_STRATEGY);
        strategyList.add(Strategy.QUOTE_STRATEGY);

        for(Strategy i : strategyList){
            strategy.add(strategyService.get(i));
        }
    }

    @Scheduled(fixedDelay = 3000)
    public void autoTrade() throws AutomationException {

        try {
            userConfigService.getAllUserConfig().forEach(user -> {
                if (!user.isTrading()) {
                    return;
                }

                CoinType coinType = user.getTradeCoin();
                double startPrice = user.getStartPrice();
                int nowLevel = user.getDifferenceLevel();
                double commonDifference = user.getCommonDifference();
                double minCash = startPrice + commonDifference * nowLevel;
                List<CandleResponse> candles = candleService.getCandles(coinType);

                CandleResponse nowCandle = candles.get(0);

                if ((tradeInfoList.size() == 0 || !nowCandle.getDateKst().equals(tradeInfoList.get(tradeInfoList.size() - 1).getDateKst()))
                        && strategy.stream().allMatch(st -> st.isProperToBuy(candles, tradeInfoList))) {
                    AccountResponse accountResponse = accountService.getKRWCurrency();

                    double myBalance = 10000000D;
//                    double myBalance = Math.ceil(Double.parseDouble(accountResponse.getBalance()));
                    // 잔고 체크
                    if (myBalance > minCash) {

                        double coinVolume = minCash / nowCandle.getTradePrice();
                        coinBalance += coinVolume;

                        TradeInfo tradeInfo = TradeInfo.builder()
                                .tradeCount(nowLevel)
                                .tradePrice(nowCandle.getTradePrice())
                                .coinVolume(coinVolume)
                                .dateKst(nowCandle.getDateKst())
                                .build();

                        tradeInfoList.add(tradeInfo);

//                        OrderRequest orderRequest = OrderRequest.builder()
//                                .market(coinType.getName())
//                                .side(Side.bid)
//                                .price(Double.toString(nowCandle.getTradePrice()))
//                                .volume(BigDecimal.valueOf(coinVolume).toString())
//                                .ordType(OrderType.limit)
//                                .build();
//
//                        orderService.requestOrder(orderRequest);
                        user.changeDifferenceLevel(user.getDifferenceLevel() + 1);
                        userConfigService.save(user);
                        log.info("매수 코인:{} 가격:{} 수량:{}",
                                coinType.getName(),
                                nowCandle.getTradePrice(),
                                BigDecimal.valueOf(coinVolume));
                    }
                } else if (tradeInfoList.size() > 0
                        && strategy.stream().allMatch(st -> st.isProperToSellWithBenefit(candles, tradeInfoList))) {

//                    AccountResponse coinResponse = accountService.getCoinCurrency(coinType);
//
//                    double coinBal = Double.parseDouble(coinResponse.getBalance());
                    double coinBal = 1000000D;

                    if (coinBal > 0) {
                        tradeInfoList.clear();
//                        OrderRequest orderRequest = OrderRequest.builder()
//                                .market(coinType.getName())
//                                .side(Side.ask)
//                                .price(Double.toString(nowCandle.getTradePrice()))
//                                .volume(BigDecimal.valueOf(coinBalance).toString())
//                                .ordType(OrderType.limit)
//                                .build();

//                        orderService.requestOrder(orderRequest);
                        user.changeDifferenceLevel(0);
                        userConfigService.save(user);

                        log.info("익절 코인:{} 가격:{}",
                                coinType.getName(),
                                nowCandle.getTradePrice());
                    }
                } else if (tradeInfoList.size() > 0
                        && strategy.stream().allMatch(st -> st.isProperToSellWithLoss(candles, tradeInfoList))) {


//                        AccountResponse coinResponse = accountService.getCoinCurrency(coinType);
//
//                        double coinBalance = Double.parseDouble(coinResponse.getBalance());
                    double coinBal = 1000000D;
                    if (coinBal > 0) {
                        tradeInfoList.clear();
//                        OrderRequest orderRequest = OrderRequest.builder()
//                                .market(coinType.getName())
//                                .side(Side.ask)
//                                .price(Double.toString(nowCandle.getTradePrice()))
//                                .volume(BigDecimal.valueOf(coinBalance).toString())
//                                .ordType(OrderType.limit)
//                                .build();
//
//                        orderService.requestOrder(orderRequest);
                        user.changeDifferenceLevel(0);

                        userConfigService.save(user);

                        log.info("손절 코인:{} 가격:{}",
                                coinType.getName(),
                                nowCandle.getTradePrice());

                    }
                }
            });
        } catch (Exception e) {
            throw new AutomationException(e, "에러");
        }

    }

}
