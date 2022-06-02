package com.joebrooks.showmethecoin.trade.upbit.backtest;

import com.joebrooks.showmethecoin.global.fee.FeeCalculator;
import com.joebrooks.showmethecoin.strategy.IStrategy;
import com.joebrooks.showmethecoin.strategy.Strategy;
import com.joebrooks.showmethecoin.strategy.StrategyService;
import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleService;
import com.joebrooks.showmethecoin.trade.upbit.client.CoinType;
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
    private final ApplicationEventPublisher applicationEventPublisher;
    private volatile boolean running = false;
    private final StrategyService strategyService;

    public void start(BackTestRequest request){
        try{
            List<TradeInfo> tradeInfoList = new LinkedList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Seoul")));
            Calendar beforeCal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Seoul")));
            List<Strategy> strategyList = new LinkedList<>();
            strategyList.add(Strategy.ADX_DMI_STRATEGY);
            strategyList.add(Strategy.RSI_STRATEGY);
//            strategyList.add(Strategy.TAIL_STRATEGY);
            strategyList.add(Strategy.PRICE_STRATEGY);
            strategyList.add(Strategy.QUOTE_STRATEGY);

            Date date = new Date();
            running = true;
            double coinBalance = 0D;
            int minute = request.getCandleMinute();
            CoinType coinType = request.getTradeCoin();
            int nowLevel = 0;
            double myBalance = request.getStartBalance();
            int divideNum = 5;
            double testBalance = myBalance / divideNum;  // test
            testBalance *= 0.95;
            double gain = 0D;
            double beforeGain = 0D;

            cal.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
            beforeCal.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
            List<IStrategy> strategy = new LinkedList<>();

            for(Strategy i : strategyList){
                strategy.add(strategyService.get(i));
            }



            while(running){

                List<CandleResponse> candles = candleService.getCandles(coinType, format.format(cal.getTime()), minute);
                CandleResponse beforeCandle = null;

                for (int i = 100; i >= 0; i--) {
                    double minCash = testBalance;

                    CandleResponse nowCandle = candles.get(i);

                    if(beforeCandle != null && nowCandle.getTimeStamp() <= beforeCandle.getTimeStamp()){
                        break;
                    }

                    List<CandleResponse> tempCandles = candles.subList(i, candles.size() - 1);

//                    BackTestResponse response = BackTestResponse.builder()
//                            .tradePrice(nowCandle.getTradePrice())
//                            .dateKst(nowCandle.getDateKst())
//                            .trade(false)
//                            .build();

                    // 구매 조건
                    if ((tradeInfoList.size() == 0 || !nowCandle.getDateKst().equals(tradeInfoList.get(0).getDateKst()))
                        && strategy.stream().allMatch(st -> st.isProperToBuy(tempCandles, tradeInfoList))) {

                        // 잔고 체크
                        if (myBalance > minCash) {

                            double coinVolume = minCash / nowCandle.getTradePrice();
                            coinBalance += coinVolume;
                            myBalance -= nowCandle.getTradePrice() * coinVolume + FeeCalculator.calculate(nowCandle.getTradePrice(), coinBalance);

//                            response = BackTestResponse.builder()
//                                    .tradePrice(nowCandle.getTradePrice())
//                                    .dateKst(nowCandle.getDateKst())
//                                    .trade(true)
//                                    .buy(true)
//                                    .build();


                            nowLevel += 1;
                            TradeInfo tradeInfo = TradeInfo.builder()
                                    .tradeCount(nowLevel)
                                    .tradePrice(nowCandle.getTradePrice())
                                    .coinVolume(coinVolume)
                                    .dateKst(nowCandle.getDateKst())
                                    .build();

                            tradeInfoList.add(tradeInfo);

//                            log.info("구매: 시각 {} 구매 횟수 {}",
//                                    nowCandle.getDateKst(),
//                                    tradeInfoList.size());
                        }
                    }

                    // 익절 조건
                    else if (tradeInfoList.size() > 0
                            && strategy.stream().allMatch(st -> st.isProperToSellWithBenefit(tempCandles, tradeInfoList))) {

                        if (coinBalance > 0) {
                            double momentGain = gain;
                            myBalance += nowCandle.getTradePrice() * coinBalance - FeeCalculator.calculate(nowCandle.getTradePrice(), coinBalance);
                            coinBalance = 0;

//                            response = BackTestResponse.builder()
//                                    .tradePrice(nowCandle.getTradePrice())
//                                    .dateKst(nowCandle.getDateKst())
//                                    .trade(true)
//                                    .build();

                            nowLevel = 0;
                            gain = myBalance - request.getStartBalance();
//                            log.info("익절: 시각 {} 이득 {} 누적 이득 {} 구매 횟수 {}",
//                                    nowCandle.getDateKst(),
//                                    gain - momentGain,
//                                    gain,
//                                    tradeInfoList.size());
                            tradeInfoList.clear();
                            testBalance = myBalance / divideNum;
                            testBalance *= 0.95;
                        }
                    }

                    // 손절 조건
                    else if (tradeInfoList.size() > 0
                            && strategy.stream().allMatch(st -> st.isProperToSellWithLoss(tempCandles, tradeInfoList))) {

                        if(coinBalance > 0) {
                            double momentGain = gain;

                            myBalance += nowCandle.getTradePrice() * coinBalance - FeeCalculator.calculate(nowCandle.getTradePrice(), coinBalance);
                            coinBalance = 0;

//                            response = BackTestResponse.builder()
//                                    .tradePrice(nowCandle.getTradePrice())
//                                    .dateKst(nowCandle.getDateKst())
//                                    .trade(true)
//                                    .build();

                            nowLevel = 0;
                            gain = myBalance - request.getStartBalance();
//                            log.info("손절: 시각 {} 이득 {} 누적 이득 {} 구매 횟수 {}",
//                                    nowCandle.getDateKst(),
//                                    gain - momentGain,
//                                    gain,
//                                    tradeInfoList.size());
                            tradeInfoList.clear();
                            testBalance = myBalance / divideNum;
                            testBalance *= 0.95;
                        }
                    }

//                    applicationEventPublisher.publishEvent(response);
                    beforeCandle = nowCandle;
                }

                cal.add(Calendar.MINUTE, minute * 100);

                if(cal.getTime().getTime() >= date.getTime()){
                    break;
                }

                if(cal.get(Calendar.DATE) != beforeCal.get(Calendar.DATE)){
                    log.info("{}월 {}일 수익: 이득 {} 잔고 {}",
                            beforeCal.get(Calendar.MONTH) + 1,
                            beforeCal.get(Calendar.DATE),
                            gain - beforeGain,
                            myBalance);
                    beforeGain = gain;
                    beforeCal.add(Calendar.DATE, 1);
                }


                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


            log.info("최종 잔고 {}, 잔여 코인{}", myBalance, coinBalance);
            applicationEventPublisher.publishEvent(BackTestResponse.builder()
                    .finish(true)
                    .gain(gain)
                    .build());
        } catch (Exception e){
            log.error("백테스팅 에러", e);
        } finally {
            running = false;
        }
    }


    public void stop(){
        running = false;
    }
}
