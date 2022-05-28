package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.global.graph.GraphStatus;
import com.joebrooks.showmethecoin.global.graph.GraphUtil;
import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component(StrategyAnnotation.ADX_DMI_STRATEGY)
@RequiredArgsConstructor
public class AdxDmiStrategy implements IStrategy{

    private final double lossRate = 0.02;
    private final double gainRate = 0.01;

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        List<ADXResponse> adxResponseList = getADXResponse(candleResponses);

        GraphStatus adxStatus = GraphUtil.getStatus(adxResponseList.get(1).getAdx(), adxResponseList.get(0).getAdx());
        double recentPlusDi = adxResponseList.get(0).getPlusDI();
        double recentMinusDi = adxResponseList.get(0).getMinusDI();

        double beforePlusDi = adxResponseList.get(1).getPlusDI();
        double beforeMinusDi = adxResponseList.get(1).getMinusDI();

        return beforeMinusDi > beforePlusDi
                && recentMinusDi > recentPlusDi
                && GraphUtil.getStatus(beforeMinusDi, recentMinusDi).equals(GraphStatus.FALLING)
                && GraphUtil.getStatus(beforePlusDi, recentPlusDi).equals(GraphStatus.FALLING)
                && adxStatus.equals(GraphStatus.RISING);

//        return Math.abs(beforeMinusDi - beforePlusDi) > Math.abs(recentMinusDi - recentPlusDi)
//                && adxStatus.equals(GraphStatus.RISING);

//        return beforeMinusDi > beforePlusDi
//                && recentMinusDi < recentPlusDi;

    }

    @Override
    public boolean isProperToSellWithBenefit(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        double averagePrice = getAveragePrice(tradeInfo);

        return (averagePrice * 1.0005) < candleResponses.get(0).getTradePrice();
    }

    @Override
    public boolean isProperToSellWithLoss(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        double averagePrice = getAveragePrice(tradeInfo);

        return (averagePrice * 1.0005) * (1 - lossRate) > candleResponses.get(0).getTradePrice(); // 실 매도시 인덱스 +1
    }

    private List<ADXResponse> getADXResponse(List<CandleResponse> candleResponses){
        BarSeries series = new BaseBarSeriesBuilder().build();

        for(int i = candleResponses.size() - 1; i >= 0; i--){
            CandleResponse response = candleResponses.get(i);

            ZonedDateTime endTime = ZonedDateTime.parse(response.getDateKst().replace('T', ' '),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul")));

            BaseBar bar = BaseBar.builder(DecimalNum::valueOf, Number.class)
                    .timePeriod(Duration.ofMinutes(240))
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

        for(int i = candleResponses.size() - 1; i >= 0; i--){
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

    private double getAveragePrice(List<TradeInfo> tradeInfo){
        double price = 0;
        double volume = 0;

        for(int i = 0; i < tradeInfo.size(); i++){
            price += tradeInfo.get(i).getTradePrice() * tradeInfo.get(i).getCoinVolume() * 1.0005;
            volume += tradeInfo.get(i).getCoinVolume();
        }

        return price / volume;
    }
}
