package com.joebrooks.showmethecoin.backtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joebrooks.showmethecoin.trade.strategy.IStrategy;
import com.joebrooks.showmethecoin.trade.strategy.StrategyService;
import com.joebrooks.showmethecoin.trade.strategy.StrategyType;
import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import com.joebrooks.showmethecoin.trade.candle.CandleMinute;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleService;
import com.joebrooks.showmethecoin.global.fee.FeeCalculator;
import com.joebrooks.showmethecoin.trade.CompanyType;
import com.joebrooks.showmethecoin.trade.candle.CandleStoreEntity;
import com.joebrooks.showmethecoin.trade.tradeinfo.TradeInfoEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackTestService {

    private final CandleService candleService;
    private final StrategyService strategyService;
    private final ObjectMapper mapper;


    public void start(BackTestRequest request, WebSocketSession session){
        try{
            List<TradeInfoEntity> tradeInfoList = new LinkedList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            double coinBalance = 0D;
            CandleMinute minute = request.getCandleMinute();
            CoinType coinType = request.getTradeCoin();
            int nowLevel = 0;
            double myBalance = request.getStartBalance();
            int maxBetCount = request.getMaxBetCount();
            double cashToBuy = myBalance / maxBetCount;
            cashToBuy *= 0.99;
            double gain = 0D;
            double beforeBalance = myBalance;
            int tradeCount = 0;
            int maxTradeCount = 0;
            double accumulatedGain = 0D;

            List<IStrategy> strategy = strategyService.get(StrategyType.BASE, request.getStrategyType());
            Calendar startDate = request.getStartDate();
            Calendar beforeCal = Calendar.getInstance(request.getStartDate().getTimeZone());



            while(true){
                List<CandleStoreEntity> candles = candleService.getCandles(coinType, format.format(startDate.getTime()), minute);
                startDate.add(Calendar.MINUTE, minute.getValue() * 100);
                int candleSize = candles.size();

                for (int i = 99; i >= 0; i--) {
                    double minCash = cashToBuy;

                    List<CandleStoreEntity> tempCandles = candles.subList(i, candleSize - 1);
                    CandleStoreEntity nowCandle = tempCandles.get(0);

                    if(format.parse(nowCandle.getDateKst().replace("T", " ")).after(request.getEndDate().getTime())){
                        break;
                    }

                    BackTestResponse response = BackTestResponse.builder()
                            .open(nowCandle.getOpeningPrice())
                            .close(nowCandle.getTradePrice())
                            .low(nowCandle.getLowPrice())
                            .high(nowCandle.getHighPrice())
                            .time(format.parse(nowCandle.getDateKst().replace('T', ' ')))
                            .traded(false)
                            .build();

                    tempCandles.set(0, nowCandle);


                    // 구매
                    if (strategy.stream().allMatch(st -> st.isProperToBuy(tempCandles, tradeInfoList))
                            && tradeInfoList.isEmpty()){

                        // 잔고 체크
                        if (myBalance >= minCash) {
                            if(nowLevel == 0){
                                beforeBalance = myBalance;
                            }

                            double coinVolume = cashToBuy / nowCandle.getTradePrice();
                            nowLevel += 1;
                            TradeInfoEntity tradeInfo = TradeInfoEntity.builder()
                                    .tradePrice(nowCandle.getTradePrice())
                                    .coinVolume(coinVolume)
                                    .orderedAt(LocalDateTime.ofInstant(format.parse(nowCandle.getDateKst().replace("T", " ")).toInstant(), ZoneId.systemDefault()))
                                    .companyType(CompanyType.UPBIT)
                                    .build();

                            tradeInfoList.add(tradeInfo);

                            coinBalance += coinVolume;
                            myBalance -= nowCandle.getTradePrice() * coinVolume
                                    + FeeCalculator.calculate(nowCandle.getTradePrice() * coinVolume, coinVolume, CompanyType.UPBIT);

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

                    // 재구매 주문
                    else if (!tradeInfoList.isEmpty()
                            && strategy.stream().allMatch(st -> st.isProperToSellWithLoss(tempCandles, tradeInfoList))
                            && tradeInfoList.size() < maxBetCount) {


                        // 잔고 체크
                        if (myBalance >= minCash) {
                            if(nowLevel == 0){
                                beforeBalance = myBalance;
                            }

                            double coinVolume = cashToBuy / nowCandle.getTradePrice();
                            nowLevel += 1;
                            TradeInfoEntity tradeInfo = TradeInfoEntity.builder()
                                    .tradePrice(nowCandle.getTradePrice())
                                    .coinVolume(coinVolume)
                                    .orderedAt(LocalDateTime.ofInstant(format.parse(nowCandle.getDateKst().replace("T", " ")).toInstant(), ZoneId.systemDefault()))
                                    .companyType(CompanyType.UPBIT)
                                    .build();

                            tradeInfoList.add(tradeInfo);

                            coinBalance += coinVolume;
                            myBalance -= nowCandle.getTradePrice() * coinVolume
                                    + FeeCalculator.calculate(nowCandle.getTradePrice() * coinVolume, coinVolume, CompanyType.UPBIT);

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
                    else if (!tradeInfoList.isEmpty()
                            && strategy.stream().allMatch(st -> st.isProperToSellWithBenefit(tempCandles, tradeInfoList))) {

                        if (coinBalance > 0) {
                            myBalance += nowCandle.getTradePrice() * coinBalance
                                    - FeeCalculator.calculate(nowCandle.getTradePrice() * coinBalance, coinBalance, CompanyType.UPBIT);
                            coinBalance = 0D;

                            response.setTraded(true);
                            response.setTradedPrice(nowCandle.getTradePrice());

                            nowLevel = 0;

//                            log.info("익절: 시각 {} 잔고 {}",
//                                    nowCandle.getDateKst(),
//                                    myBalance);
                            tradeInfoList.clear();
                            gain += myBalance - beforeBalance;
                            cashToBuy = myBalance / maxBetCount;
                            cashToBuy *= 0.99;
                            maxTradeCount = Math.max(tradeCount, maxTradeCount);
                            tradeCount = 0;
                            accumulatedGain += gain;
                        }
                    }

                    // 손절 조건
                    if (!tradeInfoList.isEmpty()
                            && strategy.stream().allMatch(st -> st.isProperToSellWithLoss(tempCandles, tradeInfoList))
                            && tradeInfoList.size() >= maxBetCount) {

                        if(coinBalance > 0) {
                            myBalance += nowCandle.getTradePrice() * coinBalance
                                    - FeeCalculator.calculate(nowCandle.getTradePrice() * coinBalance, coinBalance, CompanyType.UPBIT);
                            coinBalance = 0D;

                            response.setTraded(true);
                            response.setTradedPrice(nowCandle.getTradePrice());

                            nowLevel = 0;
                            gain += myBalance - beforeBalance;
//                            log.info("손절: 시각 {} 구매 횟수 {}",
//                                    nowCandle.getDateKst(),
//                                    tradeInfoList.size());
                            tradeInfoList.clear();
                            cashToBuy = myBalance / maxBetCount;
                            cashToBuy *= 0.99;
                            maxTradeCount = Math.max(tradeCount, maxTradeCount);
                            tradeCount = 0;
                            accumulatedGain += gain;
                        }
                    }

                    session.sendMessage(new TextMessage(mapper.writeValueAsString(response)));

                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                if(startDate.getTime().after(request.getEndDate().getTime())){
                    break;
                }

                if(startDate.get(Calendar.DATE) != beforeCal.get(Calendar.DATE)){
                    log.info("{}월 {}일 수익: 이득 {} 잔고 {} 최대 분할 횟수 {}",
                            beforeCal.get(Calendar.MONTH),
                            beforeCal.get(Calendar.DATE),
                            gain,
                            myBalance,
                            maxTradeCount);

                    beforeCal.add(Calendar.DATE, 1);
                    maxTradeCount = 0;
                    gain = 0D;

                }

            }


            log.info("최종 잔고 {}, 잔여 코인{}, 누적 이득 {}", myBalance, coinBalance, accumulatedGain);

            session.sendMessage(new TextMessage(mapper.writeValueAsString(BackTestResponse.builder()
                    .finish(true)
                    .gain(accumulatedGain)
                    .build())));


        } catch (Exception e){
            log.error("백테스팅 에러", e);
        }
    }


}
