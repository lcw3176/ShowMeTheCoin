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
        double firstHighPrice = candles.get(2).getHighPrice();
        double secondHighPrice =candles.get(1).getHighPrice();

        double firstLowPrice = candles.get(2).getLowPrice();
        double secondLowPrice =candles.get(1).getLowPrice();


        return Indicator.builder()
                .name(IndicatorType.Divergence)
                .highStatus(GraphUtil.getStatus(firstHighPrice, secondHighPrice))
                .lowStatus(GraphUtil.getStatus(firstLowPrice, secondLowPrice))
                .build();
    }
}
