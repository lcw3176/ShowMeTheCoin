package com.joebrooks.showmethecoin.upbit.indicator;

import com.joebrooks.showmethecoin.global.graph.GraphUtil;
import com.joebrooks.showmethecoin.upbit.candles.CandleResponse;
import com.joebrooks.showmethecoin.upbit.indicator.type.IndicatorAnnotation;
import com.joebrooks.showmethecoin.upbit.indicator.type.IndicatorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component(IndicatorAnnotation.RSI)
public class Rsi implements IIndicator {

    @Value("${upbit.rsi.day}")
    private int day;

    public List<Double> getRsi(List<CandleResponse> data){


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



    @Override
    public IndicatorResponse execute(List<CandleResponse> candles) {
        List<Double> rsiLst = getRsi(candles);

        double olderValue = rsiLst.get(2);
        double recentValue = rsiLst.get(1);

        return IndicatorResponse.builder()
                .type(IndicatorType.RSI)
                .values(rsiLst)
                .status(GraphUtil.getStatus(olderValue, recentValue))
                .build();
    }
}
