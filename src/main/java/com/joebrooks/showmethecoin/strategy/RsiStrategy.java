package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.global.graph.GraphStatus;
import com.joebrooks.showmethecoin.global.graph.GraphUtil;
import com.joebrooks.showmethecoin.trade.TradeInfo;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;


@Slf4j
public class RsiStrategy implements IStrategy {

    private final int buyValue = 20;

    @Override
    public boolean isProperToBuy(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
        List<Double> shortTermRsiLst = getRsi(candleResponses, 7);
        List<Double> longTermRsiLst = getRsi(candleResponses, 30);

        for(int i = 1 ; i < 10; i++){
            if(GraphUtil.getStatus(longTermRsiLst.get(i), longTermRsiLst.get(i - 1)).equals(GraphStatus.STRONG_FALLING)){
                return false;
            }
        }


        return shortTermRsiLst.get(0) > buyValue
                && shortTermRsiLst.get(0) < buyValue + 5
                && shortTermRsiLst.get(1) < buyValue;
    }

//    @Override
//    public boolean isProperToSellWithBenefit(List<CandleResponse> candleResponses, List<TradeInfo> tradeInfo) {
//        List<Double> shortTermRsiLst = getRsi(candleResponses, 7);
//
//        return shortTermRsiLst.get(0) >= 60;
//    }


    private List<Double> getRsi(List<CandleResponse> data, int count){
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
                au.add(((count - 1) * au.get(i - 1) + ups.get(i)) / count);
                ad.add(((count - 1) * ad.get(i - 1) + downs.get(i)) / count);
            }
        }


        for(int i = au.size() - 1; i >= 0; i--){
            double rs = au.get(i) / ad.get(i);
            double rsi = rs / (1 + rs);

            rsiLst.add(rsi * 100);
        }


        return rsiLst;
    }

}
