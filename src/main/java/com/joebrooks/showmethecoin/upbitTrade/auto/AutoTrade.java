package com.joebrooks.showmethecoin.upbitTrade.auto;

import com.joebrooks.showmethecoin.global.exception.type.AutomationException;
import com.joebrooks.showmethecoin.global.graph.GraphStatus;
import com.joebrooks.showmethecoin.repository.user.UserService;
import com.joebrooks.showmethecoin.upbitTrade.account.AccountResponse;
import com.joebrooks.showmethecoin.upbitTrade.account.AccountService;
import com.joebrooks.showmethecoin.upbitTrade.candles.CandleResponse;
import com.joebrooks.showmethecoin.upbitTrade.candles.CandleService;
import com.joebrooks.showmethecoin.upbitTrade.indicator.Indicator;
import com.joebrooks.showmethecoin.upbitTrade.indicator.IndicatorService;
import com.joebrooks.showmethecoin.upbitTrade.indicator.type.IndicatorType;
import com.joebrooks.showmethecoin.upbitTrade.order.OrderRequest;
import com.joebrooks.showmethecoin.upbitTrade.order.OrderService;
import com.joebrooks.showmethecoin.upbitTrade.upbit.CoinType;
import com.joebrooks.showmethecoin.upbitTrade.upbit.OrderType;
import com.joebrooks.showmethecoin.upbitTrade.upbit.Side;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoTrade {

    private AutoCommand autoCommand = AutoCommand.STOP;

    private final IndicatorService indicatorService;
    private final AccountService accountService;
    private final OrderService orderService;
    private final CandleService candleService;
    private final UserService userService;

    @Value("${auto.rsi.buy}")
    private int buy;

    @Value("${auto.rsi.sell}")
    private int sell;

    private boolean isAvailable = true;

    private final double initValue = 100000000D;
    private double lastTradePrice = initValue;
    private CandleResponse lastTradeCandle = null;


    private final IndicatorType rsiIndicator = IndicatorType.RSI;
    private final IndicatorType divergenceIndicator = IndicatorType.Divergence;

    public void setCommand(AutoCommand command){
        this.autoCommand = command;
    }



    @Scheduled(fixedDelay = 3000)
    public void mainAutomationRoutine() throws AutomationException {

        try {

            if(autoCommand.equals(AutoCommand.STOP) || !isAvailable){
                return;
            }


            userService.getAllUser().forEach(user -> {
                CoinType coinType = user.getTradeCoin();
                double startPrice = user.getStartPrice();
                int nowLevel = user.getNowLevel();

                double minCash = startPrice + 1000 * nowLevel;

                List<CandleResponse> candles = candleService.getCandles(coinType);
                Indicator rsi = indicatorService.execute(rsiIndicator, candles);
                Indicator divergence = indicatorService.execute(divergenceIndicator, candles);

                CandleResponse nowCandle = candles.get(0);

                if(rsi.getValue() <= buy
                        && rsi.getStatus().equals(GraphStatus.STRONG_RISING)
                        && divergence.getStatus().equals(GraphStatus.FALLING)
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
                        user.changeLevel(user.getNowLevel() + 1);
                        userService.save(user);
                    }

                }

                if(rsi.getValue() >= sell){

                    AccountResponse coinResponse = Arrays.stream(accountService.getAccountData())
                            .filter(data -> data.getCurrency().equals(coinType.getName().split("-")[1]))
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
                        user.changeLevel(1);
                        userService.save(user);
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
