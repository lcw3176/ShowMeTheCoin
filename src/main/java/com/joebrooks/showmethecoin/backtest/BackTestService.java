package com.joebrooks.showmethecoin.backtest;

import com.joebrooks.showmethecoin.global.fee.FeeCalculator;
import com.joebrooks.showmethecoin.strategy.IStrategy;
import com.joebrooks.showmethecoin.strategy.StrategyService;
import com.joebrooks.showmethecoin.strategy.StrategyType;
import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleService;
import com.joebrooks.showmethecoin.trade.upbit.client.CoinType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
            Calendar beforeCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
            List<StrategyType> strategyTypeList = new LinkedList<>();
//            strategyTypeList.add(StrategyType.AdxDmiStrategy);
//            strategyTypeList.add(StrategyType.RsiStrategy);
//            strategyTypeList.add(StrategyType.RmiStrategy);
//            strategyTypeList.add(StrategyType.CandleStrategy);
//            strategyTypeList.add(StrategyType.WStrategy);
            strategyTypeList.add(StrategyType.MACDStrategy);
//            strategyTypeList.add(StrategyType.TailStrategy);
            strategyTypeList.add(StrategyType.BaseStrategy);

            Date date = new Date();
            running = true;
            double coinBalance = 0D;
            int minute = request.getCandleMinute();
            CoinType coinType = request.getTradeCoin();
            int nowLevel = 0;
            double myBalance = request.getStartBalance();
            int divideNum = 6;
//            myBalance = myBalance / divideNum;  // test
            double cashToBuy = myBalance / divideNum;
            cashToBuy *= 0.99;
            double gain = 0D;
            double beforeBalance = myBalance;
            int tradeCount = 0;
            int maxTradeCount = 0;
            double accumulatedGain = 0D;

            cal.set(2022, Calendar.JULY, 1, 0, 0, 0);
            beforeCal.set(2022, Calendar.JULY, 1, 0, 0, 0);
            List<IStrategy> strategy = new LinkedList<>();

            for(StrategyType i : strategyTypeList){
                strategy.add(strategyService.get(i));
            }



            while(running){
                cal.add(Calendar.MINUTE, minute * 100);

                List<CandleResponse> candles = candleService.getCandles(coinType, format.format(cal.getTime()), minute);

                for (int i = 99; i >= 0; i--) {
                    double minCash = cashToBuy;

                    List<CandleResponse> tempCandles = candles.subList(i, candles.size() - 1);
                    CandleResponse nowCandle = tempCandles.get(0);

                    BackTestResponse response = BackTestResponse.builder()
                            .startPrice(nowCandle.getOpeningPrice())
                            .closePrice(nowCandle.getTradePrice())
                            .lowPrice(nowCandle.getLowPrice())
                            .highPrice(nowCandle.getHighPrice())
                            .dateKst(nowCandle.getDateKst())
                            .traded(false)
                            .build();

                    tempCandles.set(0, nowCandle);


                    // 구매 조건
                    if ((tradeInfoList.isEmpty() || !nowCandle.getDateKst().equals(tradeInfoList.get(tradeInfoList.size() - 1).getDateKst()))
                            && strategy.stream().allMatch(st -> st.isProperToBuy(tempCandles, tradeInfoList))) {

                        // 잔고 체크
                        if (myBalance >= minCash) {
                            if(nowLevel == 0){
                                beforeBalance = myBalance;
                            }

                            double coinVolume = cashToBuy / nowCandle.getTradePrice();
                            nowLevel += 1;
                            TradeInfo tradeInfo = TradeInfo.builder()
                                    .tradePrice(nowCandle.getTradePrice())
                                    .coinVolume(coinVolume)
                                    .dateKst(nowCandle.getDateKst())
                                    .build();

                            tradeInfoList.add(tradeInfo);

                            coinBalance += coinVolume;
                            myBalance -= nowCandle.getTradePrice() * coinVolume
                                    + FeeCalculator.calculate(nowCandle.getTradePrice() * coinVolume, coinVolume);

                            response.setBuy(true);
                            response.setTraded(true);
                            response.setTradedPrice(nowCandle.getTradePrice());


//                            log.info("구매: 시각 {} 구매 횟수 {} 구매량 {} 구매 단가 {}",
//                                    nowCandle.getDateKst(),
//                                    tradeInfoList.size(),
//                                    coinVolume,
//                                    nowCandle.getTradePrice() * coinVolume);
                            tradeCount += 1;
                        }
                    }

                    // 익절 조건
                    if (!tradeInfoList.isEmpty()
                            && strategy.stream().allMatch(st -> st.isProperToSellWithBenefit(tempCandles, tradeInfoList))) {

                        if (coinBalance > 0) {
                            myBalance += nowCandle.getTradePrice() * coinBalance
                                    - FeeCalculator.calculate(nowCandle.getTradePrice() * coinBalance, coinBalance);
                            coinBalance = 0D;

                            response.setTraded(true);
                            response.setTradedPrice(nowCandle.getTradePrice());

                            nowLevel = 0;

//                            log.info("익절: 시각 {} 잔고 {}",
//                                    nowCandle.getDateKst(),
//                                    myBalance);
                            tradeInfoList.clear();
                            gain += myBalance - beforeBalance;
                            cashToBuy = myBalance / divideNum;
                            cashToBuy *= 0.99;
                            maxTradeCount = Math.max(tradeCount, maxTradeCount);
                            tradeCount = 0;
                        }
                    }

                    // 손절 조건
                    if (!tradeInfoList.isEmpty()
                            && strategy.stream().allMatch(st -> st.isProperToSellWithLoss(tempCandles, tradeInfoList))) {

                        if(coinBalance > 0) {
                            myBalance += nowCandle.getTradePrice() * coinBalance
                                    - FeeCalculator.calculate(nowCandle.getTradePrice() * coinBalance, coinBalance);
                            coinBalance = 0D;

                            response.setTraded(true);
                            response.setTradedPrice(nowCandle.getTradePrice());

                            nowLevel = 0;
                            gain += myBalance - beforeBalance;
//                            log.info("손절: 시각 {} 구매 횟수 {}",
//                                    nowCandle.getDateKst(),
//                                    tradeInfoList.size());
                            tradeInfoList.clear();
                            cashToBuy = myBalance / divideNum;
                            cashToBuy *= 0.99;
                            maxTradeCount = Math.max(tradeCount, maxTradeCount);
                            tradeCount = 0;
                        }
                    }

                    applicationEventPublisher.publishEvent(response);
                }



                if(cal.getTime().getTime() >= date.getTime()){
                    break;
                }

                if(cal.get(Calendar.DATE) != beforeCal.get(Calendar.DATE)){
                    log.info("{}월 {}일 수익: 이득 {} 잔고 {} 최대 분할 횟수 {}",
                            beforeCal.get(Calendar.MONTH) + 1,
                            beforeCal.get(Calendar.DATE),
                            gain,
                            myBalance,
                            maxTradeCount);

                    beforeCal.add(Calendar.DATE, 1);
                    accumulatedGain += gain;
                    maxTradeCount = 0;
                    gain = 0D;

                }


                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


            log.info("최종 잔고 {}, 잔여 코인{}, 누적 이득 {}", myBalance, coinBalance, accumulatedGain);
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
