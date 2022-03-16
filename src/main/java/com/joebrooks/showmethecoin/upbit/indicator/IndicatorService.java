package com.joebrooks.showmethecoin.upbit.indicator;

import com.joebrooks.showmethecoin.upbit.candles.CandleResponse;
import com.joebrooks.showmethecoin.upbit.indicator.type.IndicatorType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndicatorService {

    private final ApplicationContext context;

    public IndicatorResponse execute(IndicatorType indicator, List<CandleResponse> candles) {
        IIndicator iIndicator = (IIndicator) context.getBean(indicator.toString());

        return iIndicator.execute(candles);
    }
}
