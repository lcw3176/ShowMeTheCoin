package com.joebrooks.showmethecoin.application.indicator;

import com.joebrooks.showmethecoin.domain.indicator.Indicator;
import com.joebrooks.showmethecoin.domain.indicator.IndicatorType;
import com.joebrooks.showmethecoin.domain.indicator.RSI;
import com.joebrooks.showmethecoin.domain.indicator.Recommend;
import com.joebrooks.showmethecoin.infra.upbit.candles.Candle;
import com.joebrooks.showmethecoin.infra.upbit.candles.CandleService;
import com.joebrooks.showmethecoin.infra.upbit.coin.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component(IndicatorType.RSI)
@RequiredArgsConstructor
public class Rsi implements IIndicator {

    private final CandleService candleService;

    private Indicator<RSI> getRsi(CoinType coinType){
        int days = 30;
        List<Candle> val = new LinkedList<>();
        candleService.getCandleData(1, 31, coinType, (val::add));

        Candle recentValue = val.get(0);

        Indicator<RSI> indicator = new Indicator<>();
        indicator.setType(IndicatorType.RSI);
        indicator.setDateKst(recentValue.getDateKst());
        indicator.setTradePrice(recentValue.getTradePrice());


        double tempUp = 0;
        double tempDown = 0;

        for(int j = 0; j < days; j++){
            double diff = val.get(j).getTradePrice() - val.get(j + 1).getTradePrice();

            if(diff > 0){
                tempUp += diff;
            } else {
                tempDown += Math.abs(diff);
            }
        }

        double rs = (tempUp / days) / (tempDown / days);
        double rsi = rs / (1 + rs);

        if(rsi <= 0.2){
            indicator.setDetailInfo(RSI.builder()
                    .rsi(rsi)
                    .recommend(Recommend.BUY)
                    .build());
        } else if (rsi >= 0.68){
            indicator.setDetailInfo(RSI.builder()
                    .rsi(rsi)
                    .recommend(Recommend.SELL)
                    .build());
        } else {
            indicator.setDetailInfo(RSI.builder()
                    .rsi(rsi)
                    .recommend(Recommend.STAY)
                    .build());
        }

        return indicator;
    }

    @Override
    public Indicator<RSI> execute(CoinType coinType) {

        return getRsi(coinType);
    }
}
