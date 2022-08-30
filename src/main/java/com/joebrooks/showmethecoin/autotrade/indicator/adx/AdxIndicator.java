package com.joebrooks.showmethecoin.autotrade.indicator.adx;

import com.joebrooks.showmethecoin.repository.candle.CandleStoreEntity;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.adx.ADXIndicator;
import org.ta4j.core.indicators.adx.MinusDIIndicator;
import org.ta4j.core.indicators.adx.PlusDIIndicator;
import org.ta4j.core.num.DecimalNum;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Component
public class AdxIndicator {


    public List<AdxResponse> getAdx(List<CandleStoreEntity> candleResponses){
        BarSeries series = new BaseBarSeriesBuilder().build();

        for(int i = candleResponses.size() - 1; i >= 0; i--){
            CandleStoreEntity response = candleResponses.get(i);

            ZonedDateTime endTime = ZonedDateTime.parse(response.getDateKst().replace('T', ' '),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul")));

            BaseBar bar = BaseBar.builder(DecimalNum::valueOf, Number.class)
                    .timePeriod(Duration.ofMinutes(1))
                    .endTime(endTime)
                    .openPrice(response.getOpeningPrice())
                    .highPrice(response.getHighPrice())
                    .lowPrice(response.getLowPrice())
                    .closePrice(response.getTradePrice())
                    .volume(response.getAccTradeVolume())
                    .build();
            series.addBar(bar);
        }

        ADXIndicator adxIndicator = new ADXIndicator(series, 14, 14);
        MinusDIIndicator minusDIIndicator = new MinusDIIndicator(series, 14);
        PlusDIIndicator plusDIIndicator = new PlusDIIndicator(series, 14);

        List<AdxResponse> lst = new LinkedList<>();

        for(int i = candleResponses.size() - 1; i >= candleResponses.size() - 5; i--){
            lst.add(AdxResponse.builder()
                    .adx(adxIndicator.getValue(i).doubleValue())
                    .plusDI(plusDIIndicator.getValue(i).doubleValue())
                    .minusDI(minusDIIndicator.getValue(i).doubleValue())
                    .build());
        }

        return lst;
    }
}
