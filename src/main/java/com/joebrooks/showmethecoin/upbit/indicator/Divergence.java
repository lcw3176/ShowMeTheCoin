package com.joebrooks.showmethecoin.upbit.indicator;

import com.joebrooks.showmethecoin.upbit.candles.CandleResponse;
import com.joebrooks.showmethecoin.global.graph.GraphUtil;
import com.joebrooks.showmethecoin.upbit.indicator.IIndicator;
import com.joebrooks.showmethecoin.upbit.indicator.IndicatorResponse;
import com.joebrooks.showmethecoin.upbit.indicator.type.IndicatorAnnotation;
import com.joebrooks.showmethecoin.upbit.indicator.type.IndicatorType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(IndicatorAnnotation.Divergence)
public class Divergence implements IIndicator {

    @Override
    public IndicatorResponse execute(List<CandleResponse> candles) {
        double firstPrice = candles.get(2).getTradePrice();
        double secondPrice = candles.get(1).getTradePrice();


        return IndicatorResponse.builder()
                .type(IndicatorType.Divergence)
                .status(GraphUtil.getStatus(firstPrice, secondPrice))
                .build();
    }
}
