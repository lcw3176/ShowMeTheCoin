package com.joebrooks.showmethecoin.trade.indicator.macd;


import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Component
public class MacdIndicator {

    public List<MacdResponse> getMacd(List<CandleStoreEntity> candleResponses){
        BarSeries series = new BaseBarSeriesBuilder().build();

        for(int i = candleResponses.size() - 1; i >= 0; i--){
            CandleStoreEntity response = candleResponses.get(i);

            ZonedDateTime endTime = ZonedDateTime.parse(response.getDateKst().replace('T', ' '),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul")));

            series.addBar(endTime,
                    response.getOpeningPrice(),
                    response.getHighPrice(),
                    response.getLowPrice(),
                    response.getTradePrice(),
                    response.getAccTradeVolume());

        }
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        MACDIndicator macdIndicator = new MACDIndicator(closePrice, 12, 26);
        EMAIndicator signalIndicator = new EMAIndicator(macdIndicator, 9);

        List<MacdResponse> lst = new LinkedList<>();

        for(int i = candleResponses.size() - 1; i >= candleResponses.size() - 100; i--){
            lst.add(MacdResponse.builder()
                    .macd(macdIndicator.getValue(i).doubleValue())
                    .signal(signalIndicator.getValue(i).doubleValue())
                    .build());
        }

        return lst;
    }
}
