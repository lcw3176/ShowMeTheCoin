package com.joebrooks.showmethecoin.upbitTrade.indicator.type;

import com.joebrooks.showmethecoin.upbitTrade.candles.CandleResponse;
import com.joebrooks.showmethecoin.global.graph.GraphUtil;
import com.joebrooks.showmethecoin.upbitTrade.indicator.Indicator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(IndicatorAnnotation.Divergence)
public class Divergence implements IIndicator {

    @Override
    public Indicator execute(List<CandleResponse> candles) {
        double firstPrice = candles.get(2).getTradePrice();
        double secondPrice = candles.get(1).getTradePrice();


        return Indicator.builder()
                .type(IndicatorType.Divergence)
                .status(GraphUtil.getStatus(firstPrice, secondPrice))
                .build();
    }
}
