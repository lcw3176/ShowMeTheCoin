package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.candles.CandleResponse;
import com.joebrooks.showmethecoin.candles.CandleService;
import com.joebrooks.showmethecoin.common.upbit.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component(StrategyType.RSIWithDivergence)
@RequiredArgsConstructor
public class RsiWithDivergenceIndicator implements IStrategy {


    private final CandleService candleService;
    private final RsiIndicator rsiIndicator;

    @Value("${day}")
    private int days;

    @Value("${minute}")
    private int minute;

    private GraphStatus getTradePriceStatus(List<CandleResponse> val){
        double firstXVal = 1;
        double firstYVal = val.get(val.size() / 2 - 1).getTradePrice();

        double recentXVal = val.size();
        double recentYVal = val.get(0).getTradePrice();

        return (recentYVal - firstYVal) / (recentXVal - firstXVal) > 0 ?
                GraphStatus.RISING : GraphStatus.FALLING;
    }

    @Override
    public Strategy execute(CoinType coinType) {
        List<CandleResponse> val = new LinkedList<>(
                Arrays.asList(candleService.getCandleData(minute, days, coinType)));

        List<Double> rsi = rsiIndicator.getRsi(val, days);

        Strategy strategy = new Strategy();
        strategy.setMostRecentRsi(rsi.get(0));
        strategy.setRsiStatus(rsiIndicator.getRsiStatus(rsi));
        strategy.setPriceStatus(getTradePriceStatus(val));

        return strategy;
    }
}
