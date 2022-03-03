package com.joebrooks.showmethecoin.indicator;

import com.joebrooks.showmethecoin.candles.CandleService;
import com.joebrooks.showmethecoin.global.upbit.CoinType;
import com.joebrooks.showmethecoin.indicator.type.IIndicator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IndicatorService {

    private final ApplicationContext context;
    private final CandleService candleService;

    public List<Indicator> execute(String indicators, CoinType coinType) {

        List<Indicator> ls = new LinkedList<>();

        if(!indicators.contains(",")){
            IIndicator iIndicator = (IIndicator) context.getBean(indicators);

            ls.add(iIndicator.execute(candleService.getCandles(coinType)));

        } else {
            String[] indArr = indicators.split(",");

            for(String i : indArr){
                IIndicator iIndicator = (IIndicator) context.getBean(i);
                ls.add(iIndicator.execute(candleService.getCandles(coinType)));
            }
        }

        return ls;
    }
}
