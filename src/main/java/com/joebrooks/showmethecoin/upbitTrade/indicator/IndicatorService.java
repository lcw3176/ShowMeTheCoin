package com.joebrooks.showmethecoin.upbitTrade.indicator;

import com.joebrooks.showmethecoin.upbitTrade.candles.CandleResponse;
import com.joebrooks.showmethecoin.upbitTrade.indicator.type.IndicatorType;
import com.joebrooks.showmethecoin.upbitTrade.upbit.CoinType;
import com.joebrooks.showmethecoin.upbitTrade.indicator.type.IIndicator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IndicatorService {

    private final ApplicationContext context;
//    private final CandleService candleService;
//candleService.getCandles(coinType)
    public List<Indicator> execute(List<IndicatorType> indicators, List<CandleResponse> candles) {

        List<Indicator> ls = new LinkedList<>();

        indicators.forEach(i -> {
            IIndicator iIndicator = (IIndicator) context.getBean(i.toString());

            ls.add(iIndicator.execute(candles));
        });
//        if(!indicators.contains(",")){
//            IIndicator iIndicator = (IIndicator) context.getBean(indicators);
//
//            ls.add(iIndicator.execute(candles));
//
//        } else {
//            String[] indArr = indicators.split(",");
//
//            for(String i : indArr){
//                IIndicator iIndicator = (IIndicator) context.getBean(i);
//                ls.add(iIndicator.execute(candles));
//            }
//        }

        return ls;
    }
}
