package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.Builder;
import lombok.Getter;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class MACDStrategy implements IStrategy {


    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        List<MACDResponse> macdResponseList = getMACDResponse(candleResponses);
//        double minValue = 0;
//
//        for(int i = 1; i < 20; i++){
//            minValue = Math.min(macdResponseList.get(i).getSignal(), minValue);
//
//        }
//        minValue < macdResponseList.get(0).getSignal()
        return macdResponseList.get(0).getSignal() < 0
                && macdResponseList.get(0).getMacd() < 0;
    }

    private List<MACDResponse> getMACDResponse(List<CandleResponse> candleResponses){
        BarSeries series = new BaseBarSeriesBuilder().build();

        for(int i = candleResponses.size() - 1; i >= 0; i--){
            CandleResponse response = candleResponses.get(i);

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
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        MACDIndicator macdIndicator = new MACDIndicator(closePrice, 12, 26);
        EMAIndicator signalIndicator = new EMAIndicator(macdIndicator, 9);

        List<MACDResponse> lst = new LinkedList<>();

        for(int i = candleResponses.size() - 1; i >= candleResponses.size() - 100; i--){
            lst.add(MACDResponse.builder()
                    .macd(macdIndicator.getValue(i).doubleValue())
                    .signal(signalIndicator.getValue(i).doubleValue())
                    .build());
        }

        return lst;
    }


    @Getter
    @Builder
    static class MACDResponse{
        private double macd;
        private double signal;
    }
}
