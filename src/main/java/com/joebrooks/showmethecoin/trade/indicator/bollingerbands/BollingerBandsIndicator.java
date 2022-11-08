package com.joebrooks.showmethecoin.trade.indicator.bollingerbands;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.num.DecimalNum;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Component
public class BollingerBandsIndicator {

    public List<BollingerBandsResponse> getBollingerBands(List<CandleStoreEntity> candleResponses){

        BarSeries series = new BaseBarSeriesBuilder().build();

        for(int i = candleResponses.size() - 1; i >= 0; i--){
            CandleStoreEntity response = candleResponses.get(i);

            ZonedDateTime endTime = ZonedDateTime.parse(response.getDateKst().replace('T', ' '),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul")));

            series.addBar(BaseBar.builder(DecimalNum::valueOf, Number.class)
                    .timePeriod(Duration.ofMinutes(5))
                    .endTime(endTime)
                    .openPrice(response.getOpeningPrice())
                    .highPrice(response.getHighPrice())
                    .lowPrice(response.getLowPrice())
                    .closePrice(response.getTradePrice())
                    .volume(response.getAccTradeVolume())
                    .build());
        }

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        EMAIndicator avg20 = new EMAIndicator(closePrice, 20);
        StandardDeviationIndicator sd20 = new StandardDeviationIndicator(closePrice, 20);

        // Bollinger bands
        BollingerBandsMiddleIndicator middleBBand = new BollingerBandsMiddleIndicator(avg20);
        BollingerBandsLowerIndicator lowBBand = new BollingerBandsLowerIndicator(middleBBand, sd20);
        BollingerBandsUpperIndicator upBBand = new BollingerBandsUpperIndicator(middleBBand, sd20);


        List<BollingerBandsResponse> lst = new LinkedList<>();

        for(int i = candleResponses.size() - 1; i >= 0; i--){
            lst.add(BollingerBandsResponse.builder()
                    .upper(upBBand.getValue(i).doubleValue())
                    .middle(middleBBand.getValue(i).doubleValue())
                    .lower(lowBBand.getValue(i).doubleValue())
                    .build());
        }

        return lst;
    }
}
