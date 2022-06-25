package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.global.graph.GraphStatus;
import com.joebrooks.showmethecoin.global.graph.GraphUtil;
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

    private int count = 0;

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        List<MACDResponse> macdResponseList = getMACDResponse(candleResponses);

//        if( macdResponseList.get(1).getSignal() > macdResponseList.get(1).getMacd()
//                && macdResponseList.get(0).getSignal() < macdResponseList.get(0).getMacd()
//                && macdResponseList.get(0).getSignal() < 0
//                && macdResponseList.get(0).getMacd() < 0){
//            count++;
//        }
//
//        if(macdResponseList.get(1).getSignal() < macdResponseList.get(1).getMacd()
//                && macdResponseList.get(0).getSignal() > macdResponseList.get(0).getMacd()
//                && macdResponseList.get(0).getSignal() > 0
//                && macdResponseList.get(0).getMacd() > 0){
//            count = 0;
//        }

        return  macdResponseList.get(1).getSignal() > macdResponseList.get(1).getMacd()
                && macdResponseList.get(0).getSignal() < macdResponseList.get(0).getMacd()
                && macdResponseList.get(0).getSignal() < -55000
                && macdResponseList.get(0).getMacd() < -55000;
    }

//    @Override
//    public boolean isProperToSellWithBenefit(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
//        List<MACDResponse> macdResponseList = getMACDResponse(candleResponses);
//
//
//        return macdResponseList.get(1).getSignal() < macdResponseList.get(1).getMacd()
//                && macdResponseList.get(0).getSignal() > macdResponseList.get(0).getMacd()
//                && macdResponseList.get(0).getSignal() > 0
//                && macdResponseList.get(0).getMacd() > 0;
//    }


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

        for(int i = candleResponses.size() - 1; i >= candleResponses.size() - 5; i--){
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
