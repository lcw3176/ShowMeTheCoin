package com.joebrooks.showmethecoin.trade.backtest;

import com.joebrooks.showmethecoin.global.fee.FeeCalculator;
import com.joebrooks.showmethecoin.global.util.TimeFormatter;
import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.CompanyType;
import com.joebrooks.showmethecoin.trade.strategy.IStrategy;
import com.joebrooks.showmethecoin.trade.strategy.StrategyService;
import com.joebrooks.showmethecoin.trade.strategy.StrategyType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BackTestCore {

    private final StrategyService strategyService;


    public BackTestResponse execute(BackTestInnerModel model, List<CandleStoreEntity> candleStoreEntityList, StrategyType strategyType, List<TradeInfoEntity> tradeInfoList) {

        try {

            double myBalance = model.getStartBalance();
            int maxBetCount = model.getMaxBetCount();
            double cashToBuy = myBalance / maxBetCount;

            List<IStrategy> strategy = strategyService.get(StrategyType.BASE, strategyType);

            CandleStoreEntity nowCandle = candleStoreEntityList.get(0);

            BackTestResponse response = BackTestResponse.builder()
                    .open(nowCandle.getOpeningPrice())
                    .close(nowCandle.getTradePrice())
                    .low(nowCandle.getLowPrice())
                    .high(nowCandle.getHighPrice())
                    .time(TimeFormatter.convert(nowCandle.getDateKst().replace('T', ' ')))
                    .traded(false)
                    .build();
            // 구매
            if (strategy.stream().allMatch(st -> st.isProperToBuy(candleStoreEntityList, tradeInfoList))
                    && tradeInfoList.isEmpty()) {

                // 잔고 체크
                if (myBalance >= cashToBuy) {
                    double coinVolume = cashToBuy / nowCandle.getTradePrice();
                    TradeInfoEntity tradeInfo = TradeInfoEntity.builder()
                            .tradePrice(nowCandle.getTradePrice())
                            .coinVolume(coinVolume)
                            .orderedAt(
                                    LocalDateTime.ofInstant(
                                            TimeFormatter.convert(nowCandle.getDateKst().replace("T", " "))
                                                    .toInstant(), ZoneId.systemDefault()))
                            .companyType(CompanyType.UPBIT)
                            .build();

                    tradeInfoList.add(tradeInfo);

                    model.setCoinBalance(model.getCoinBalance() + coinVolume);
                    model.setBalance(model.getBalance() - coinVolume * nowCandle.getTradePrice());

                    response.setBuy(true);
                    response.setTraded(true);
                    response.setTradedPrice(nowCandle.getTradePrice());

                    log.info("구매: 시각 {} 구매 횟수 {} 구매 단가 {} 구매량 {} 잔고 {}",
                            nowCandle.getDateKst(),
                            tradeInfoList.size(),
                            nowCandle.getTradePrice(),
                            coinVolume,
                            myBalance);
                }
            }

            // 재구매 주문
            else if (!tradeInfoList.isEmpty()
                    && strategy.stream().allMatch(st -> st.isProperToSellWithLoss(candleStoreEntityList, tradeInfoList))
                    && tradeInfoList.size() < maxBetCount) {

                // 잔고 체크
                if (myBalance >= cashToBuy) {
                    double coinVolume = cashToBuy / nowCandle.getTradePrice();
                    TradeInfoEntity tradeInfo = TradeInfoEntity.builder()
                            .tradePrice(nowCandle.getTradePrice())
                            .coinVolume(coinVolume)
                            .orderedAt(LocalDateTime.ofInstant(
                                    TimeFormatter.convert(nowCandle.getDateKst().replace("T", " "))
                                            .toInstant(), ZoneId.systemDefault()))
                            .companyType(CompanyType.UPBIT)
                            .build();

                    tradeInfoList.add(tradeInfo);

                    model.setCoinBalance(model.getCoinBalance() + coinVolume);
                    model.setBalance(model.getBalance() - coinVolume * nowCandle.getTradePrice());

                    response.setBuy(true);
                    response.setTraded(true);
                    response.setTradedPrice(nowCandle.getTradePrice());

                    log.info("구매: 시각 {} 구매 횟수 {} 구매 단가 {} 구매량 {} 잔고 {}",
                            nowCandle.getDateKst(),
                            tradeInfoList.size(),
                            nowCandle.getTradePrice(),
                            coinVolume,
                            myBalance);

                }
            }

            // 익절 조건
            else if (!tradeInfoList.isEmpty()
                    && strategy.stream().allMatch(st -> st.isProperToSellWithBenefit(candleStoreEntityList, tradeInfoList))) {

                if (model.getCoinBalance() > 0) {
                    myBalance += getGain(candleStoreEntityList, tradeInfoList);

                    model.setCoinBalance(0D);
                    model.setGain(myBalance);

                    response.setTraded(true);
                    response.setTradedPrice(nowCandle.getTradePrice());

                    log.info("익절: 시각 {} 잔고 {}",
                            nowCandle.getDateKst(),
                            myBalance);
                    tradeInfoList.clear();
                }
            }

            // 손절 조건
            else if (!tradeInfoList.isEmpty()
                    && strategy.stream().allMatch(st -> st.isProperToSellWithLoss(candleStoreEntityList, tradeInfoList))
                    && tradeInfoList.size() >= maxBetCount) {

                if (model.getCoinBalance() > 0) {
                    myBalance += getGain(candleStoreEntityList, tradeInfoList);
                    model.setGain(myBalance);
                    model.setCoinBalance(0D);

                    response.setTraded(true);
                    response.setTradedPrice(nowCandle.getTradePrice());

                    log.info("손절: 시각 {} 잔고 {}",
                            nowCandle.getDateKst(),
                            myBalance);
                    tradeInfoList.clear();

                }
            }

            return response;

        } catch (Exception e) {
            log.error("백테스팅 에러", e);

            return BackTestResponse.builder().build();
        }

    }


    private double getGain(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        double averageSellPrice = getAverageSellPrice(candleResponses, tradeInfo);
        double payingFee = getPayingFee(candleResponses, tradeInfo);

        return averageSellPrice - payingFee;
    }


    private double getAverageSellPrice(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        double volume = 0;

        for (TradeInfoEntity i : tradeInfo) {
            volume += i.getCoinVolume();
        }

        return volume * candleResponses.get(0).getTradePrice();
    }

    private double getPayingFee(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
        double volume = 0;

        for (TradeInfoEntity i : tradeInfo) {
            volume += i.getCoinVolume();
        }

        return FeeCalculator.calculate(candleResponses.get(0).getTradePrice(), volume,
                tradeInfo.get(0).getCompanyType());
    }

}
