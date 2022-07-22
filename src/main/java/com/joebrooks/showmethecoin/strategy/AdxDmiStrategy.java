package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.global.graph.GraphStatus;
import com.joebrooks.showmethecoin.global.graph.GraphUtil;
import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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


@RequiredArgsConstructor
public class AdxDmiStrategy implements IStrategy{

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        List<ADXResponse> adxResponseList = getADXResponse(candleResponses);

        GraphStatus adxStatus = GraphUtil.getStatus(adxResponseList.get(1).getAdx(), adxResponseList.get(0).getAdx());
        double recentPDI = adxResponseList.get(0).getPlusDI();
        double recentMDI = adxResponseList.get(0).getMinusDI();

        double beforePDI = adxResponseList.get(1).getPlusDI();
        double beforeMDI = adxResponseList.get(1).getMinusDI();
//        for(int i = 1; i < 4; i++){
//            if(GraphUtil.getStatus(adxResponseList.get(i).getMinusDI(), adxResponseList.get(i - 1).getMinusDI()).equals(GraphStatus.STRONG_RISING)){
//                return false;
//            }
//        }
//recentMDI > recentPDI
        return
//                && recentMDI < adxResponseList.get(0).getAdx()
//                && adxResponseList.get(0).getAdx() - recentMDI < 5
//                GraphUtil.getStatus(beforeMDI, recentMDI).equals(GraphStatus.FALLING)
                GraphUtil.getStatus(beforePDI, recentPDI).equals(GraphStatus.RISING)
                && recentPDI * 4 < recentMDI;
    }


//    @Override
//    public boolean isProperToSellWithBenefit(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo){
//        List<ADXResponse> adxResponseList = getADXResponse(candleResponses);
//
//        GraphStatus adxStatus = GraphUtil.getStatus(adxResponseList.get(1).getAdx(), adxResponseList.get(0).getAdx());
//        double recentPDI = adxResponseList.get(0).getPlusDI();
//        double recentMDI = adxResponseList.get(0).getMinusDI();
//
//        double beforePDI = adxResponseList.get(1).getPlusDI();
//
//        return  recentMDI < recentPDI
////                recentPDI > adxResponseList.get(0).getAdx()
////                && GraphUtil.getStatus(beforePDI, recentPDI).equals(GraphStatus.RISING)
////                && adxResponseList.get(0).getAdx() - recentPDI < 5;
//                && adxStatus.equals(GraphStatus.RISING)
//                && recentMDI * 5 < recentPDI;
//    }



    private List<ADXResponse> getADXResponse(List<CandleResponse> candleResponses){
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

        ADXIndicator adxIndicator = new ADXIndicator(series, 14, 14);
        MinusDIIndicator minusDIIndicator = new MinusDIIndicator(series, 14);
        PlusDIIndicator plusDIIndicator = new PlusDIIndicator(series, 14);

        List<ADXResponse> lst = new LinkedList<>();

        for(int i = candleResponses.size() - 1; i >= candleResponses.size() - 5; i--){
            lst.add(ADXResponse.builder()
                    .adx(adxIndicator.getValue(i).doubleValue())
                    .plusDI(plusDIIndicator.getValue(i).doubleValue())
                    .minusDI(minusDIIndicator.getValue(i).doubleValue())
                    .build());
        }

        return lst;
    }


    @Getter
    @Builder
    static class ADXResponse{
        private double plusDI;
        private double minusDI;
        private double adx;
    }

}
