package com.joebrooks.showmethecoin.trade.indicator.rsi;

import com.joebrooks.showmethecoin.trade.candle.CandleStoreEntity;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class RsiIndicator {

    public List<RsiResponse> getRsi(List<CandleStoreEntity> data, int count){
        List<RsiResponse> rsiLst = new LinkedList<>();
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
            if(au.isEmpty()){
                au.add(ups.get(i));
                ad.add(downs.get(i));
            } else {
                au.add(((count - 1) * au.get(i - 1) + ups.get(i)) / count);
                ad.add(((count - 1) * ad.get(i - 1) + downs.get(i)) / count);
            }
        }


        for(int i = au.size() - 1; i >= 0; i--){
            double rs = au.get(i) / ad.get(i);
            double rsi = rs / (1 + rs);

            rsiLst.add(RsiResponse.builder()
                    .rsi(rsi * 100)
                    .build());
        }


        return rsiLst;
    }
}
