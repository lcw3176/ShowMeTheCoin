package com.joebrooks.showmethecoin.trade.indicator.rmi;

import com.joebrooks.showmethecoin.repository.candle.CandleStoreEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class RmiIndicator {

    public List<RmiResponse> getRmi(List<CandleStoreEntity> data, int day, int n){
        List<CandleStoreEntity> copyData = new LinkedList<>(data);

        Collections.copy(copyData, data);
        Collections.reverse(copyData);

        List<RmiResponse> rmiLst = new LinkedList<>();
        List<Double> ups = new LinkedList<>();
        List<Double> downs = new LinkedList<>();

        List<Double> mu = new LinkedList<>();
        List<Double> md = new LinkedList<>();

        for(int i = n; i < copyData.size(); i++){
            double diff = copyData.get(i).getTradePrice() - copyData.get(i - n).getTradePrice();

            if(diff > 0){
                ups.add(diff);
                downs.add(0D);
            } else {
                downs.add(Math.abs(diff));
                ups.add(0D);
            }
        }

        for(int i = day; i < ups.size() + 1; i++){
            double tempUp = 0D;
            double tempDown = 0D;

            for(int j = i - day; j < i; j++){
                tempUp += ups.get(j);
                tempDown += downs.get(j);
            }

            mu.add(tempUp / day);
            md.add(tempDown / day);
        }


        for(int i = mu.size() - 1; i >= 0; i--){
            double rm = mu.get(i) / md.get(i);
            double rmi = rm / (1 + rm);

            rmiLst.add(RmiResponse.builder()
                    .rmi(rmi * 100)
                    .build());
        }

        return rmiLst;
    }
}
