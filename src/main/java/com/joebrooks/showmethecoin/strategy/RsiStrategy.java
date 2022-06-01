package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.global.graph.GraphStatus;
import com.joebrooks.showmethecoin.global.graph.GraphUtil;
import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component(StrategyAnnotation.RSI_STRATEGY)
@Slf4j
public class RsiStrategy implements IStrategy {

    private final int day = 14;
    private final int buyValue = 30;
    private final int sellValue = 60;

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        List<Double> rsi = getRsi(candleResponses);

        for(int i = 1 ; i < 6; i++){
            if(GraphUtil.getStatus(rsi.get(i), rsi.get(i - 1)).equals(GraphStatus.STRONG_FALLING)){
                return false;
            }
        }
//
//        return rsi.get(0) < buyValue;

        return rsi.get(0) > buyValue && rsi.get(1) < buyValue;
    }

//    @Override
//    public boolean isProperToSellWithBenefit(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
//        List<Double> rsi = getRsi(candleResponses);
//
//        return rsi.get(0) > sellValue;
//    }


    private List<Double> getRsi(List<CandleResponse> data){
        List<Double> rsiLst = new LinkedList<>();
        List<Double> ups = new LinkedList<>();
        List<Double> downs = new LinkedList<>();

        List<Double> au = new LinkedList<>();
        List<Double> ad = new LinkedList<>();

        for(int i = data.size() - 2; i >= 0; i--){
            double diff = data.get(i).getTradePrice() - data.get(i + 1).getTradePrice();

            if(diff > 0){
                ups.add(diff);
                downs.add(0D);
            } else {
                downs.add(Math.abs(diff));
                ups.add(0D);
            }
        }

        for(int i = 0; i < ups.size(); i++){
            if(au.size() == 0){
                au.add(ups.get(i));
                ad.add(downs.get(i));
            } else {
                au.add(((day - 1) * au.get(i - 1) + ups.get(i)) / day);
                ad.add(((day - 1) * ad.get(i - 1) + downs.get(i)) / day);
            }
        }


        for(int i = au.size() - 1; i >= 0; i--){
            double rs = au.get(i) / ad.get(i);
            double rsi = rs / (1 + rs);

            rsiLst.add(rsi * 100);
        }


        return rsiLst;
    }


//    private List<Double> getRsi(List<CandleResponse> candleResponses){
//        BarSeries series = new BaseBarSeriesBuilder().build();
//
//        for(int i = candleResponses.size() - 1; i >= 0; i--){
//            CandleResponse response = candleResponses.get(i);
//
//            ZonedDateTime endTime = ZonedDateTime.parse(response.getDateKst().replace('T', ' '),
//                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul")));
//
//            BaseBar bar = BaseBar.builder(DecimalNum::valueOf, Number.class)
//                    .timePeriod(Duration.ofMinutes(240))
//                    .endTime(endTime)
//                    .openPrice(response.getOpeningPrice())
//                    .highPrice(response.getHighPrice())
//                    .lowPrice(response.getLowPrice())
//                    .closePrice(response.getTradePrice())
//                    .volume(response.getAccTradeVolume())
//                    .build();
//            series.addBar(bar);
//        }
//
//        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(series);
//
//        RSIIndicator rsiIndicator = new RSIIndicator(closePriceIndicator, 14);
//        List<Double> lst = new LinkedList<>();
//
//        for(int i = candleResponses.size() - 1; i >= candleResponses.size() - 5; i--){
//            lst.add(rsiIndicator.getValue(i).doubleValue());
//        }
//
//        return lst;
//    }
}
