package com.joebrooks.showmethecoin.trade.upbit.backtest;

import com.joebrooks.showmethecoin.global.exception.type.AutomationException;
import com.joebrooks.showmethecoin.strategy.IStrategy;
import com.joebrooks.showmethecoin.strategy.StrategyService;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleService;
import com.joebrooks.showmethecoin.trade.upbit.client.CoinType;
import com.joebrooks.showmethecoin.trade.upbit.indicator.IndicatorResponse;
import com.joebrooks.showmethecoin.trade.upbit.indicator.IndicatorService;
import com.joebrooks.showmethecoin.trade.upbit.indicator.type.IndicatorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackTestService {

    private final CandleService candleService;
    private final IndicatorService indicatorService;
    private CandleResponse lastTradeCandle = null;
    private final ApplicationEventPublisher applicationEventPublisher;
    private volatile boolean running = false;
    private final StrategyService strategyService;

    public void start(BackTestRequest request){
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Seoul")));

            Date date = new Date();
            running = true;
            double coinBalance = 0D;
            int minute = request.getCandleMinute();
            CoinType coinType = request.getTradeCoin();
            double startPrice = request.getStartPrice();
            int nowLevel = 0;
            double commonDifference = request.getCommonDifference();
            double myBalance = request.getStartBalance();
            double minCash = startPrice + commonDifference * nowLevel;

            cal.set(2022, Calendar.APRIL, 1, 0, 0, 0);
            IStrategy strategy = strategyService.get(request.getStrategy());


            while(running){
                List<CandleResponse> candles = candleService.getCandles(coinType, format.format(cal.getTime()), minute);
                List<IndicatorResponse> indicators = new LinkedList<>();

                for(IndicatorType type : strategy.getRequiredIndicators()){
                    indicators.add(indicatorService.execute(type, candles));
                }


                for (int i = 100; i >= 0; i--) {
                    CandleResponse nowCandle = candles.get(i);

                    if(lastTradeCandle != null && lastTradeCandle.getTimeStamp() > nowCandle.getTimeStamp()){
                        continue;
                    }

                    BackTestResponse response = BackTestResponse.builder()
                            .tradePrice(nowCandle.getTradePrice())
                            .dateKst(nowCandle.getDateKst())
                            .trade(false)
                            .build();

                    // 구매 조건
                    if ((lastTradeCandle == null || !nowCandle.getDateKst().equals(lastTradeCandle.getDateKst()))
                        && strategy.isProperToBuy(indicators, candles)) {

                        // 잔고 && 마지막 거래 가격 체크
                        if (myBalance > minCash &&
                                (lastTradeCandle == null || nowCandle.getTradePrice() < lastTradeCandle.getTradePrice())) {

                            double coinVolume = minCash / nowCandle.getTradePrice();
                            coinBalance += coinVolume;
                            myBalance -= nowCandle.getTradePrice() * coinVolume * 1.0005;
                            response = BackTestResponse.builder()
                                    .tradePrice(nowCandle.getTradePrice())
                                    .dateKst(nowCandle.getDateKst())
                                    .trade(true)
                                    .buy(true)
                                    .build();

                            lastTradeCandle = nowCandle;
                            nowLevel += 1;
                        }
                    }

                    // 익절 조건
                    else if (strategy.isProperToSellWithBenefit(indicators, candles)) {

                        if (coinBalance > 0) {
                            myBalance += nowCandle.getTradePrice() * coinBalance * 0.9995;
                            coinBalance = 0;
                            response = BackTestResponse.builder()
                                    .tradePrice(nowCandle.getTradePrice())
                                    .dateKst(nowCandle.getDateKst())
                                    .trade(true)
                                    .build();

                            nowLevel = 0;

                        }
                    }

                    // 손절 조건
                    else if (lastTradeCandle != null && strategy.isProperToSellWithLoss(indicators, candles, lastTradeCandle)) {

                        if(coinBalance > 0) {
                            myBalance += nowCandle.getTradePrice() * coinBalance * 0.9995;
                            coinBalance = 0;
                            response = BackTestResponse.builder()
                                    .tradePrice(nowCandle.getTradePrice())
                                    .dateKst(nowCandle.getDateKst())
                                    .trade(true)
                                    .build();
                            nowLevel = 0;

                        }
                    }

                    if(i == 0){
                        response.setFinish(true);
                    }

                    applicationEventPublisher.publishEvent(response);

                    indicators = clearValues(indicators);
                }

                cal.add(Calendar.MINUTE, minute * 100);

                if(cal.getTime().getTime() >= date.getTime()){
                    break;
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            log.info("잔고 {}, 잔여 코인{}", myBalance, coinBalance);
        } catch (Exception e){
            log.error("백테스팅 에러", e);
        }
    }

    private List<IndicatorResponse> clearValues(List<IndicatorResponse> indicators){

        for(IndicatorResponse res: indicators){
            if(res.getType().equals(IndicatorType.RSI) && res.getValues().size() > 1){
                res.getValues().remove(0);
            }
        }

        return indicators;
    }

    public void stop(){
        running = false;
    }
}
