package com.joebrooks.showmethecoin.strategy;


import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import com.joebrooks.showmethecoin.trade.upbit.indicator.IndicatorResponse;
import com.joebrooks.showmethecoin.trade.upbit.indicator.type.IndicatorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component(StrategyAnnotation.RSI_STRATEGY)
@RequiredArgsConstructor
public class RsiStrategy implements IStrategy {

    private final int buy = 30;
    private final int sell = 60;

    @Override
    public boolean isProperToBuy(List<IndicatorResponse> response, List<CandleResponse> candleResponses) {
//        IndicatorResponse rsi = response.stream()
//                .filter(i -> i.getType().equals(IndicatorType.RSI))
//                .findAny()
//                .orElseThrow(() -> {
//                    throw new RuntimeException("rsi 지표 확인 불가");
//                });
//
//
//        double mostRecentValue = rsi.getValues().get(0);
//        double beforeValue = rsi.getValues().get(1);
//
//        return beforeValue < buy &&
//                (mostRecentValue > buy && mostRecentValue < (double) (buy + sell) / 2);

        IndicatorResponse rsi = response.stream()
                .filter(i -> i.getType().equals(IndicatorType.RSI))
                .findAny()
                .orElseThrow(() -> {
                    throw new RuntimeException("rsi 지표 확인 불가");
                });

        return rsi.getValues().get(0) < buy;

    }

    @Override
    public boolean isProperToSellWithBenefit(List<IndicatorResponse> response, List<CandleResponse> candleResponses) {
        IndicatorResponse rsi = response.stream()
                .filter(i -> i.getType().equals(IndicatorType.RSI))
                .findAny()
                .orElseThrow(() -> {
                    throw new RuntimeException("rsi 지표 확인 불가");
                });

        return rsi.getValues().get(0) >= sell;
    }

    @Override
    public boolean isProperToSellWithLoss(List<IndicatorResponse> response, List<CandleResponse> candleResponses, CandleResponse lastTradeCandle) {
//        return lastTradeCandle.getTradePrice() * 0.993 >= candleResponses.get(0).getTradePrice();
        return false;
    }

    @Override
    public List<IndicatorType> getRequiredIndicators() {
        return List.of(IndicatorType.RSI);
    }

}
