package com.joebrooks.showmethecoin.upbitTrade.auto;

import com.joebrooks.showmethecoin.global.exception.type.AutomationException;
import com.joebrooks.showmethecoin.global.graph.GraphStatus;
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

    @Value("${auto.rsi.buy}")
    private int buy;

    @Value("${auto.rsi.sell}")
    private int sell;

    private final double initCashValue = 10000D;
    private double minCash = initCashValue;
    private double weight = 0.1D;
    private boolean isAvailable = true;

    private final double initValue = 100000000D;
    private double lastTradePrice = initValue;
    private CandleResponse lastTradeCandle = null;


    private final CoinType coinType = CoinType.WAVES;
    private final IndicatorType rsiIndicator = IndicatorType.RSI;
    private final IndicatorType divergenceIndicator = IndicatorType.Divergence;

    public void setCommand(AutoCommand command){
        this.autoCommand = command;
    }



    @Scheduled(fixedDelay = 3000)
    public void mainAutomationRoutine() throws AutomationException {

        try{
            if(autoCommand.equals(AutoCommand.STOP) || !isAvailable){
                return;
            }

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
                            .volume(Double.toString(coinVolume))
                            .ordType(OrderType.limit)
                            .build();

                    orderService.requestOrder(orderRequest);

                    log.info("{}: 매수 {}", coinType.getKoreanName(), nowCandle.getTradePrice());

                    lastTradePrice = nowCandle.getTradePrice();
                    lastTradeCandle = nowCandle;

                    minCash = initCashValue + (weight * initCashValue);
                    weight += 0.1D;
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
                            .volume(Double.toString(coinBalance))
                            .ordType(OrderType.limit)
                            .build();

                    orderService.requestOrder(orderRequest);

                    log.info("{}: 매도 {}", coinType.getKoreanName(), nowCandle.getTradePrice());
                    lastTradePrice = initValue;
                    minCash = initCashValue;
                    weight = 0.1D;
                }
            }

        } catch (ReadTimeoutException e){
            log.info("타임아웃");
        } catch (Exception e){
            isAvailable = false;
            throw new AutomationException(e, "작동 정지");
        }
    }

}
