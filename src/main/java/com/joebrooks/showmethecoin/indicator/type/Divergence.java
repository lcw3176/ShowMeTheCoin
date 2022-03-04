package com.joebrooks.showmethecoin.indicator.type;

import com.joebrooks.showmethecoin.candles.CandleResponse;
import com.joebrooks.showmethecoin.global.graph.GraphUtil;
import com.joebrooks.showmethecoin.indicator.Indicator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(IndicatorType.Divergence)
public class Divergence implements IIndicator {

    @Override
    public Indicator execute(List<CandleResponse> candles) {
        double firstPriceVal = candles.get(1).getTradePrice();
        double secondPriceYVal =candles.get(0).getTradePrice();

        return Indicator.builder()
                .name(IndicatorType.Divergence)
                .status(GraphUtil.getStatus(firstPriceVal, secondPriceYVal))
                .build();
    }
}
