package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class RmiStrategy implements IStrategy {

    private final int buyValue = 40;

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        List<Double> rmiLst = getRmi(candleResponses, 60, 5);

        return rmiLst.get(0) > buyValue;
    }


    private List<Double> getRmi(List<CandleResponse> data, int day, int n){
        List<CandleResponse> copyData = new LinkedList<>(data);

        Collections.copy(copyData, data);
        Collections.reverse(copyData);

        List<Double> rmiLst = new LinkedList<>();
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

            rmiLst.add(rmi * 100);
        }

        return rmiLst;
    }

}
